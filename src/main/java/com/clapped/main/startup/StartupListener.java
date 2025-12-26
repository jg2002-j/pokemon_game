package com.clapped.main.startup;

import com.clapped.main.messaging.events.SettingsEvent;
import com.clapped.main.messaging.producer.SettingsEventProducer;
import com.clapped.main.service.GameState;
import com.clapped.pokemon.service.PokemonService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Slf4j
@ApplicationScoped
public class StartupListener {

    private final GameState gameState;
    private final SettingsEventProducer settingsEventProducer;
    private final PokemonService pokemonService;

    @Inject
    public StartupListener(final GameState gameState, final SettingsEventProducer settingsEventProducer, final PokemonService pokemonService) {
        this.gameState = gameState;
        this.settingsEventProducer = settingsEventProducer;
        this.pokemonService = pokemonService;
    }

    void init(@Observes final StartupEvent startupEvent) {
        log.info("\u001B[32mApplication Version: {}\u001B[0m", System.getenv("BUILD_VERSION"));
        sendInitialGameInfo();
        pokemonService.populateAllTypes();
    }

    private void sendInitialGameInfo() {
        final LinkedList<String> logMsgs = new LinkedList<>();
        logMsgs.add("Initialised Pokemon Generation to " + gameState.getPokemonGen());
        logMsgs.add("Initialised Pokemon Level to " + gameState.getPokemonLevel());
        settingsEventProducer.sendSettingsEvent(new SettingsEvent(
                gameState.getPokemonGen().getNumber(),
                gameState.getPokemonLevel(),
                logMsgs
        ));
    }

}