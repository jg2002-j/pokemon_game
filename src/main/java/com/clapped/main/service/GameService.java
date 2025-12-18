package com.clapped.main.service;

import com.clapped.boundary.rest.dto.GameSettingsDto;
import com.clapped.main.messaging.events.EventType;
import com.clapped.main.messaging.events.GameEvent;
import com.clapped.main.messaging.events.GameEvtType;
import com.clapped.main.messaging.producer.GameEventProducer;
import com.clapped.main.model.ProcessResult;
import com.clapped.pokemon.model.Generation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class GameService {

    private final GameState state;
    private final TurnService turnService;
    private final GameEventProducer gameEventProducer;

    @Inject
    public GameService(final GameState state, final TurnService turnService, final GameEventProducer gameEventProducer) {
        this.state = state;
        this.turnService = turnService;
        this.gameEventProducer = gameEventProducer;
    }

    private boolean isGameSettingsUnlocked() {
        return state.getAllPlayers().isEmpty();
    }

    public List<ProcessResult> updateSettings(final GameSettingsDto dto) {
        log.info("{}", dto);
        return List.of(
            changeGlobalLevel(dto.getLevel()),
            changeGlobalGen(dto.getGen())
        );
    }

    private ProcessResult changeGlobalLevel(final int newLvl) {
        if (isGameSettingsUnlocked()) {
            if (newLvl <= 100 && newLvl > 0) {
                state.setPokemonLevel(newLvl);
                gameEventProducer.sendGameEvent(new GameEvent(
                    System.currentTimeMillis(),
                    EventType.GAME_EVENT,
                    GameEvtType.LEVEL_CHANGE,
                    newLvl,
                    ProcessResult.success("Pokemon Level successfully updated to " + newLvl)
                ));
                return ProcessResult.success("Pokemon level now set to " + newLvl);
            } else {
                return ProcessResult.error("Please choose a level between 1 and 100 (inclusive).");
            }
        }
        return ProcessResult.error("Pokemon level cannot be changed now.");
    }

    private ProcessResult changeGlobalGen(final int gen) {
        if (isGameSettingsUnlocked()) {
            final Optional<Generation> newGenerationChoice = Arrays.stream(Generation.values())
                .filter(generation -> generation.getNumericalVal() == gen)
                .findFirst();
            if (newGenerationChoice.isPresent()) {
                final Generation newGeneration = newGenerationChoice.get();
                state.setPokemonGen(newGeneration);
                final ProcessResult res = ProcessResult.success("Pokemon Generation now set to " + newGeneration);
                gameEventProducer.sendGameEvent(new GameEvent(
                    System.currentTimeMillis(),
                    EventType.GAME_EVENT,
                    GameEvtType.GENERATION_CHANGE,
                    newGeneration.getNumericalVal(),
                    res
                ));
                return res;
            }
            return ProcessResult.error("Invalid option, please choose a generation between 1 and 9.");
        }
        return ProcessResult.error("Pokemon generation cannot be changed now.");
    }

    public ProcessResult finaliseTeams() {
        if (turnService.getTurnNum() > 0) {
            return ProcessResult.error("Game already started, all players must submit turn options to move to the next turn.");
        }
        if (state.getAllPlayers().size() < 2) {
            return ProcessResult.error("Please add 2 or more players.");
        }
        return turnService.startTurn();
    }

}
