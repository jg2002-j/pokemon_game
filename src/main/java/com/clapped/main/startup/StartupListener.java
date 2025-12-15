package com.clapped.main.startup;

import com.clapped.main.messaging.producer.GameEventProducer;
import com.clapped.main.service.GameState;
import com.clapped.pokemon.service.PokemonService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class StartupListener {

    private final GameState gameState;
    private final GameEventProducer gameEventProducer;
    private final PokemonService pokemonService;

    @Inject
    public StartupListener(final GameState gameState, final GameEventProducer gameEventProducer, final PokemonService pokemonService) {
        this.gameState = gameState;
        this.gameEventProducer = gameEventProducer;
        this.pokemonService = pokemonService;
    }

    void init(@Observes final StartupEvent startupEvent) {
        log.info("\u001B[32mApplication Version: {}\u001B[0m", System.getenv("BUILD_VERSION"));
        populateTypes();
    }

    private void populateTypes() {
        pokemonService.populateTypes();
    }

}