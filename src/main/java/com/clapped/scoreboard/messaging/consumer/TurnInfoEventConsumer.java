package com.clapped.scoreboard.messaging.consumer;

import com.clapped.main.messaging.events.TurnInfoEvent;
import com.clapped.scoreboard.ScoreboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class TurnInfoEventConsumer {

    private final ScoreboardService service;

    @Inject
    public TurnInfoEventConsumer(final ScoreboardService service) {
        this.service = service;
    }

    @Incoming("turn-info-in")
    public void consume(final TurnInfoEvent evt) {
        service.handleTurnInfoEvent(evt);
    }

}
