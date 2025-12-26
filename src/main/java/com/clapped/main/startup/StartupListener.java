package com.clapped.main.startup;

import com.clapped.main.model.ProcessResult;
import com.clapped.main.service.GameState;
import com.clapped.pokemon.service.PokemonService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import static com.clapped.main.messaging.events.EventType.GAME_EVENT;
import static com.clapped.main.messaging.events.GameEvtType.GENERATION_CHANGE;
import static com.clapped.main.messaging.events.GameEvtType.LEVEL_CHANGE;

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
        sendInitialGameInfo();
        populateTypes();
    }

    private void sendInitialGameInfo() {
        gameEventProducer.sendGameEvent(new GameEvent(
                System.currentTimeMillis(),
                GAME_EVENT,
                GENERATION_CHANGE,
                gameState.getPokemonGen().getNumber(),
                ProcessResult.success("Initialised Pokemon Generation as " + gameState.getPokemonGen())
        ));
        gameEventProducer.sendGameEvent(new GameEvent(
                System.currentTimeMillis(),
                GAME_EVENT,
                LEVEL_CHANGE,
                gameState.getPokemonLevel(),
                ProcessResult.success("Initialised Pokemon Level as " + gameState.getPokemonLevel())
        ));
    }

    private void populateTypes() {
        pokemonService.populateAllTypes();
    }

}