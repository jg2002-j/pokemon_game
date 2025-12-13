package com.clapped.main.service;

import com.clapped.main.messaging.events.GameEvent;
import com.clapped.main.messaging.events.GameEvtType;
import com.clapped.main.messaging.producer.GameEventProducer;
import com.clapped.main.model.ProcessResult;
import com.clapped.pokemon.model.Generation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Arrays;
import java.util.Optional;

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

    public ProcessResult changeGlobalLevel(final int newLvl) {
        if (newLvl <= 100 && newLvl > 0) {
            state.setPokemonLevel(newLvl);
            gameEventProducer.sendGameEvent(new GameEvent(
                    System.currentTimeMillis(),
                    GameEvtType.LEVEL_CHANGE,
                    newLvl,
                    ProcessResult.success("Pokemon Level successfully updated to " + newLvl)
            ));
            return ProcessResult.success("Pokemon level now set to " + newLvl);
        } else {
            return ProcessResult.error("Please choose a level between 1 and 100 (inclusive).");
        }
    }

    public ProcessResult changeGlobalGen(final int gen) {
        final Optional<Generation> newGenerationChoice = Arrays.stream(Generation.values())
                .filter(generation -> generation.getNumericalVal() == gen)
                .findFirst();
        if (newGenerationChoice.isPresent()) {
            final Generation newGeneration = newGenerationChoice.get();
            state.setPokemonGen(newGeneration);
            final ProcessResult res = ProcessResult.success("Pokemon Generation now set to " + newGeneration);
            gameEventProducer.sendGameEvent(new GameEvent(
                    System.currentTimeMillis(),
                    GameEvtType.GENERATION_CHANGE,
                    newGeneration.getNumericalVal(),
                    res
            ));
            return res;
        }
        return ProcessResult.error("Invalid option, please choose a generation between 1 and 9.");
    }

    public ProcessResult toggleShowdownIcons(final boolean show) {
        state.setUseShowdownIcons(show);
        return ProcessResult.success("Showdown icons are now toggled " + (show ? "on" : "off") + ". Note: this only affects newly added Pokemon.");
    }

    public ProcessResult finaliseTeams() {
        if (turnService.getTurnNum() == 0) {
            return turnService.startTurn();
        } else {
            return ProcessResult.error("Game already started, all players must submit turn options to move to the next turn.");
        }
    }
}
