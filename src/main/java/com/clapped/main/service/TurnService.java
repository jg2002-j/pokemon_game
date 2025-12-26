package com.clapped.main.service;

import com.clapped.main.messaging.events.TurnQueueEvent;
import com.clapped.main.messaging.events.TurnStartEvent;
import com.clapped.main.messaging.producer.TurnQueueEventProducer;
import com.clapped.main.messaging.producer.TurnStartEventProducer;
import com.clapped.main.model.*;
import com.clapped.pokemon.model.Medicine;
import com.clapped.pokemon.model.Move;
import com.clapped.pokemon.model.Pokemon;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static com.clapped.main.model.ActionType.*;
import static com.clapped.pokemon.model.pokemon.PokemonStat.SPEED;

@Slf4j
@ApplicationScoped
public class TurnService {

    private static final String ERROR_PREFIX = "ERROR: ";
    @Getter
    private int turnNum = 0;
    private final List<PlayerAction> queuedActions = new ArrayList<>();
    private final Map<String, List<ActionType>> playerTurnOptions = new HashMap<>();

    private final GameState gameState;
    private final BattleEngine battleEngine;

    private final TurnStartEventProducer turnStartEventProducer;
    private final TurnQueueEventProducer turnQueueEventProducer;

    @Inject
    public TurnService(
            final GameState gameState,
            final BattleEngine battleEngine,
            final TurnStartEventProducer turnStartEventProducer,
            final TurnQueueEventProducer turnQueueEventProducer
    ) {
        this.gameState = gameState;
        this.battleEngine = battleEngine;
        this.turnStartEventProducer = turnStartEventProducer;
        this.turnQueueEventProducer = turnQueueEventProducer;
    }

    // 1. Start a new turn

    public ProcessResult startTurn() {
        turnNum++;
        final String msg = "Turn " + turnNum + " started.";
        populatePlayerActionChoices();
        turnStartEventProducer.sendTurnStartEvent(new TurnStartEvent(
                turnNum,
                playerTurnOptions,
                msg
        ));
        queueWaitActions();
        return ProcessResult.success(msg);
    }

    private void populatePlayerActionChoices() {
        for (final Player player : gameState.getAllPlayers()) {
            final String username = player.getUsername();
            final Pokemon currentPokemon = player.getPokemon();
            if (currentPokemon.isFainted()) {
                boolean hasNonFaintedPokemon = player.getPokemonTeam().stream().anyMatch(pokemon -> !pokemon.isFainted());
                if (hasNonFaintedPokemon) {
                    playerTurnOptions.put(username, List.of(SWITCH));
                } else {
                    playerTurnOptions.put(username, List.of(NONE));
                }
            } else if (currentPokemon.getMoveRemainingTurns() > 0) {
                playerTurnOptions.put(username, List.of(WAIT));
            } else {
                playerTurnOptions.put(username, List.of(SWITCH, ATTACK, HEAL));
            }
        }
    }

    private void queueWaitActions() {
        for (Player player : gameState.getAllPlayers()) {
            if (playerTurnOptions.get(player.getUsername()).contains(WAIT)) {
                queuedActions.add(new WaitAction(player));
            }
        }
    }

// 2. Players submit actions for turn

    public List<ProcessResult> attackAction(final String attackerUsername, final String targetUsername, final String moveName) {
        try {
            final ProcessResult validateAttackOutcome = validateAttack(attackerUsername, targetUsername, moveName);
            if (validateAttackOutcome != null) return List.of(validateAttackOutcome);

            final Player attackerPlayer = gameState.getPlayer(attackerUsername);
            final Move usedMove = getMoveFromPokemonByName(attackerPlayer.getPokemon(), moveName);
            final Player targetPlayer = gameState.getPlayer(targetUsername);

            final AttackAction action = new AttackAction(attackerPlayer, usedMove, targetPlayer);
            return addAndCompleteAction(action);
        } catch (Exception ex) {
            log.error(ERROR_PREFIX, ex);
            return List.of(ProcessResult.error("Failed to handle attack turn: " + ex.getMessage()));
        }
    }

    private ProcessResult validateAttack(final String attackerUser, final String targetUser, final String moveName) {
        final ProcessResult commonValidationResult = commonValidateAction(attackerUser, ATTACK);
        if (commonValidationResult != null) return commonValidationResult;
        if (!gameState.hasPlayer(targetUser)) {
            return ProcessResult.error("'" + targetUser + "' could not be found.");
        }
        final Pokemon attackerPokemon = gameState.getPlayer(attackerUser).getPokemon();
        final Pokemon targetPokemon = gameState.getPlayer(targetUser).getPokemon();
        final Move usedMove = getMoveFromPokemonByName(attackerPokemon, moveName);
        if (usedMove == null) {
            return ProcessResult.error(moveName + " is an invalid choice for " + attackerPokemon.getName().toUpperCase() + ".");
        }
        if (attackerPokemon.isFainted()) {
            return ProcessResult.error(attackerUser + "'s " + attackerPokemon.getName().toUpperCase() + " is fainted.");
        }
        if (targetPokemon.isFainted()) {
            return ProcessResult.error(targetUser + "'s " + targetPokemon.getName().toUpperCase() + " is already fainted!");
        }
        return null;
    }

    private Move getMoveFromPokemonByName(final Pokemon pokemon, final String moveName) {
        return pokemon.getMoves().stream()
                .filter(move -> moveName.equals(move.getName()))
                .findFirst()
                .orElse(null);
    }

// ------------------------

    public List<ProcessResult> healAction(final String username, final String medicineName) {
        try {
            final ProcessResult validateHealOutcome = validateHeal(username, medicineName);
            if (validateHealOutcome != null) return List.of(validateHealOutcome);

            final Player player = gameState.getPlayer(username);
            final Medicine medicine = Medicine.valueOf(medicineName);

            HealAction action = new HealAction(player, medicine);
            return addAndCompleteAction(action);
        } catch (Exception ex) {
            log.error(ERROR_PREFIX, ex);
            return List.of(ProcessResult.error("Failed to handle heal turn: " + ex.getMessage()));
        }
    }

    private ProcessResult validateHeal(final String username, final String medicineName) {
        final ProcessResult commonValidationResult = commonValidateAction(username, HEAL);
        if (commonValidationResult != null) return commonValidationResult;

        if (getMedicineByName(medicineName) == null) {
            return ProcessResult.error("'" + medicineName + "' is an invalid choice.");
        }

        final Pokemon pokemon = gameState.getPlayer(username).getPokemon();
        final Medicine medicine = Medicine.valueOf(medicineName);

        if (!medicine.hasEffectOnPokemon(pokemon)) {
            return ProcessResult.error(medicine.name() + " has no effect on " + pokemon.getName().toUpperCase() + "...");
        }
        return null;
    }

    private Medicine getMedicineByName(final String medicineName) {
        try {
            return Medicine.valueOf(medicineName);
        } catch (Exception ex) {
            log.error(ERROR_PREFIX, ex);
            return null;
        }
    }

// ------------------------

    public List<ProcessResult> switchAction(String username, int newIndex) {
        try {
            final ProcessResult commonValidationResult = commonValidateAction(username, SWITCH);
            if (commonValidationResult != null) return List.of(commonValidationResult);

            final Player player = gameState.getPlayer(username);

            final ProcessResult switchValidationResult = validateSwitch(player, newIndex);
            if (switchValidationResult != null) return List.of(switchValidationResult);

            final SwitchAction action = new SwitchAction(player, newIndex);
            return addAndCompleteAction(action);
        } catch (Exception ex) {
            log.error(ERROR_PREFIX, ex);
            return List.of(ProcessResult.error("Failed to handle switch turn: " + ex.getMessage()));
        }
    }

    private ProcessResult validateSwitch(final Player player, final int newIndex) {
        final LinkedList<Pokemon> team = player.getPokemonTeam();
        if (newIndex < 0 || newIndex >= team.size()) {
            return ProcessResult.error("Invalid Pokemon index: " + newIndex);
        }
        final Pokemon targetPokemon = team.get(newIndex);
        if (targetPokemon == null) {
            return ProcessResult.error("No Pokemon at index " + newIndex);
        }
        if (targetPokemon.isFainted()) {
            return ProcessResult.error(targetPokemon.getName().toUpperCase() + " is fainted and cannot be switched in.");
        }
        if (player.getPokemon() == targetPokemon) {
            return ProcessResult.error(targetPokemon.getName().toUpperCase() + " is already active.");
        }
        return null;
    }

// ------------------------

    public ProcessResult commonValidateAction(String turnUsername, ActionType actionType) {
        if (turnNum == 0) {
            return ProcessResult.error("Please finalise teams before submitting player actions.");
        }
        if (!gameState.hasPlayer(turnUsername)) {
            return ProcessResult.error("Could not find '" + turnUsername + "'");
        }
        final Player player = gameState.getPlayer(turnUsername);
        if (playerTurnOptions.get(player.getUsername()).contains(NONE)) {
            return ProcessResult.error("No actions can be taken at this time, entire team is fainted!");
        }
        if (!playerTurnOptions.get(player.getUsername()).contains(actionType)) {
            return ProcessResult.error("Invalid option for " + turnUsername + ", must be one of: " + playerTurnOptions.get(player));
        }
        return null;
    }

    private List<ProcessResult> addAndCompleteAction(final PlayerAction action) {
        List<ProcessResult> results = new ArrayList<>();
        queuedActions.add(action);
        results.add(ProcessResult.success(action.toPrettyString()));
        if (allPlayersQueuedAction()) {
            results.addAll(endTurn());
        } else {
            log.info("{}/{} turns taken, waiting for {}.", queuedActions.size(), gameState.getAllPlayers().size(), getUsernamesWithoutActions());
            turnQueueEventProducer.sendTurnQueueEvent(new TurnQueueEvent(
                    turnNum,
                    getUsernamesAndActions(),
                    getUsernamesWithoutActions(),
                    action.toPrettyString()
            ));
        }
        return results;
    }

// 3. End existing turn

    public List<ProcessResult> endTurn() {
        List<ProcessResult> results = new ArrayList<>();
        results.addAll(processActions());
        results.add(startTurn());
        return results;
    }

    private List<ProcessResult> processActions() {
        final LinkedList<ProcessResult> processResults = new LinkedList<>();
        for (final PlayerAction action : getSortedTurns()) {
            if (action instanceof SwitchAction switchAction) {
                processResults.add(battleEngine.switchout(switchAction));
            } else if (action instanceof HealAction healAction) {
                processResults.add(battleEngine.heal(healAction));
            } else if (action instanceof AttackAction attackAction) {
                processResults.add(battleEngine.attack(attackAction));
            } else if (action instanceof WaitAction waitAction) {
                processResults.add(battleEngine.wait(waitAction));
            }
        }
        queuedActions.clear();
        if (!processResults.isEmpty() && processResults.stream().allMatch(ProcessResult::isSuccess)) {
            log.info("Turn {} completed successfully", turnNum);
        } else {
            log.error("Turn {} could not be completed", turnNum);
        }
        return processResults;
    }

// ------------------------

    private Queue<PlayerAction> getSortedTurns() {
        return queuedActions.stream().sorted((turn1, turn2) -> {
                    // SwitchActions come first
                    boolean isSwitch1 = turn1 instanceof SwitchAction;
                    boolean isSwitch2 = turn2 instanceof SwitchAction;

                    if (isSwitch1 && !isSwitch2) return -1;
                    if (!isSwitch1 && isSwitch2) return 1;

                    // HealActions come second
                    boolean isHeal1 = turn1 instanceof HealAction;
                    boolean isHeal2 = turn2 instanceof HealAction;

                    if (isHeal1 && !isHeal2) return -1;
                    if (!isHeal1 && isHeal2) return 1;

                    // AttackActions and WaitActions sorted by priority then speed
                    Move move1 = getActionMove(turn1);
                    Move move2 = getActionMove(turn2);

                    if (move1 != null && move2 != null) {
                        // Sort by priority first (higher priority first)
                        int priorityCompare = Integer.compare(move2.getPriority(), move1.getPriority());
                        if (priorityCompare != 0) return priorityCompare;

                        // Then by speed (higher speed first)
                        int speed1 = turn1 instanceof AttackAction attack1
                                ? attack1.getAttackerPlayer().getPokemon().getBaseStats().get(SPEED)
                                : ((WaitAction) turn1).getPlayer().getPokemon().getBaseStats().get(SPEED);
                        int speed2 = turn2 instanceof AttackAction attack2
                                ? attack2.getAttackerPlayer().getPokemon().getBaseStats().get(SPEED)
                                : ((WaitAction) turn2).getPlayer().getPokemon().getBaseStats().get(SPEED);
                        return Integer.compare(speed2, speed1);
                    }

                    return 0;
                })
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Move getActionMove(PlayerAction action) {
        if (action instanceof AttackAction attackAction) {
            return attackAction.getUsedMove();
        } else if (action instanceof WaitAction waitAction) {
            return waitAction.getPlayer().getPokemon().getCurrentlyUsedMove();
        }
        return null;
    }

    private boolean allPlayersQueuedAction() {
        final List<String> allPlayerUsers = gameState.getAllPlayers().stream().map(Player::getUsername).toList();
        for (String player : allPlayerUsers) {
            if (queuedActions.stream().noneMatch(turn -> turn.getTurnUser().equals(player))) {
                return false;
            }
        }
        return true;
    }

    private Map<String, String> getUsernamesAndActions() {
        Map<String, String> usernamesWithActions = new HashMap<>();
        for (final PlayerAction action : queuedActions) {
            usernamesWithActions.put(action.getTurnUser(), action.toPrettyString());
        }
        return usernamesWithActions;
    }

    private List<String> getUsernamesWithoutActions() {
        return gameState.getAllPlayers().stream()
                .map(Player::getUsername)
                .filter(username -> queuedActions.stream()
                        .noneMatch(action -> action.getTurnUser().equals(username)))
                .collect(Collectors.toList());
    }
}
