package com.clapped.scoreboard.messaging.consumer;

import com.clapped.main.messaging.events.TurnActionEvent;
import com.clapped.scoreboard.ScoreboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class TurnActionEventConsumer {

    private final ScoreboardService service;

    @Inject
    public TurnActionEventConsumer(final ScoreboardService service) {
        this.service = service;
    }

    @Incoming("turn-action-in")
    public void consume(final TurnActionEvent evt) {
        service.handleTurnActionEvent(evt);
    }

}