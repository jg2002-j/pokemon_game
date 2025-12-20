package com.clapped.scoreboard.messaging.consumer;

import com.clapped.main.messaging.events.GameEvent;
import com.clapped.scoreboard.ScoreboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class GameEventConsumer {

    private final ScoreboardService service;

    @Inject
    public GameEventConsumer(final ScoreboardService service) {
        this.service = service;
    }

    @Incoming("game-in")
    public void consume(final GameEvent evt) {
        service.handleGameEvent(evt);
    }

}