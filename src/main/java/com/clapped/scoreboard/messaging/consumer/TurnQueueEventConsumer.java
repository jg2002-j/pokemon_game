package com.clapped.scoreboard.messaging.consumer;

import com.clapped.main.messaging.events.TurnQueueEvent;
import com.clapped.scoreboard.ScoreboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class TurnQueueEventConsumer {
    private final ScoreboardService service;

    @Inject
    public TurnQueueEventConsumer(final ScoreboardService service) {
        this.service = service;
    }

    @Incoming("turnqueue-in")
    public void consume(final TurnQueueEvent evt) {
        service.handleTurnQueueEvent(evt);
    }
}
