package com.clapped.scoreboard.messaging.consumer;

import com.clapped.main.messaging.events.TurnStartEvent;
import com.clapped.scoreboard.ScoreboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class TurnStartEventConsumer {
    private final ScoreboardService service;

    @Inject
    public TurnStartEventConsumer(final ScoreboardService service) {
        this.service = service;
    }

    @Incoming("turnstart-in")
    public void consume(final TurnStartEvent evt) {
        service.handleTurnStartEvent(evt);
    }
}
