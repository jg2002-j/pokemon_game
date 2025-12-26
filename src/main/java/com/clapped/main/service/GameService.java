package com.clapped.main.service;

import com.clapped.boundary.rest.dto.GameSettingsDto;
import com.clapped.main.messaging.events.SettingsEvent;
import com.clapped.main.messaging.producer.SettingsEventProducer;
import com.clapped.main.model.ProcessResult;
import com.clapped.pokemon.model.Generation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class GameService {

    private final GameState state;
    private final TurnService turnService;
    private final SettingsEventProducer settingsEventProducer;

    @Inject
    public GameService(final GameState state, final TurnService turnService, final SettingsEventProducer settingsEventProducer) {
        this.state = state;
        this.turnService = turnService;
        this.settingsEventProducer = settingsEventProducer;
    }

    private boolean isGameSettingsUnlocked() {
        return state.getAllPlayers().isEmpty();
    }

    public List<ProcessResult> updateSettings(final GameSettingsDto dto) {
        log.info("{}", dto);
        final List<ProcessResult> results = List.of(
                changeGlobalLevel(dto.getLevel()),
                changeGlobalGen(dto.getGen())
        );
        if (results.stream().allMatch(ProcessResult::isSuccess)) {
            final LinkedList<String> logMsgs = results.stream()
                    .map(ProcessResult::getMessage)
                    .collect(Collectors.toCollection(LinkedList::new));
            settingsEventProducer.sendSettingsEvent(new SettingsEvent(
                    state.getPokemonGen().getNumber(),
                    state.getPokemonLevel(),
                    logMsgs
            ));
        }
        return results;
    }

    private ProcessResult changeGlobalLevel(final int newLvl) {
        if (isGameSettingsUnlocked()) {
            if (newLvl <= 100 && newLvl > 0) {
                state.setPokemonLevel(newLvl);
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
                    .filter(generation -> generation.getNumber() == gen)
                    .findFirst();
            if (newGenerationChoice.isPresent()) {
                final Generation newGeneration = newGenerationChoice.get();
                state.setPokemonGen(newGeneration);
                return ProcessResult.success("Pokemon Generation now set to " + newGeneration);
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
