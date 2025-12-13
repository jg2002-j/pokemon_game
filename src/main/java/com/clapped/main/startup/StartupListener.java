package com.clapped.main.startup;

import com.clapped.main.messaging.events.GameEvent;
import com.clapped.main.messaging.events.GameEvtType;
import com.clapped.main.messaging.producer.GameEventProducer;
import com.clapped.main.model.ProcessResult;
import com.clapped.main.service.GameState;
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

    @Inject
    public StartupListener(final GameState gameState, final GameEventProducer gameEventProducer) {
        this.gameState = gameState;
        this.gameEventProducer = gameEventProducer;
    }

    void init(@Observes final StartupEvent startupEvent) {
        log.info("\u001B[32mApplication Version: {}\u001B[0m", System.getenv("BUILD_VERSION"));
        sendInitialGameInfo();
    }

    private void sendInitialGameInfo() {
        int defaultGen = gameState.getPokemonGen().getNumericalVal();
        gameEventProducer.sendGameEvent(new GameEvent(
                System.currentTimeMillis(),
                GameEvtType.GENERATION_CHANGE,
                defaultGen,
                ProcessResult.success("Initial generation set to " + defaultGen)
        ));

        int defaultLevel = gameState.getPokemonLevel();
        gameEventProducer.sendGameEvent(new GameEvent(
                System.currentTimeMillis(),
                GameEvtType.LEVEL_CHANGE,
                defaultLevel,
                ProcessResult.success("Initial level set to " + defaultLevel)
        ));
    }
}