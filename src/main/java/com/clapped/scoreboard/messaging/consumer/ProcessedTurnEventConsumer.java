package com.clapped.scoreboard.messaging.consumer;

import com.clapped.main.messaging.events.ProcessedTurnEvent;
import com.clapped.scoreboard.ScoreboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ProcessedTurnEventConsumer {
    private final ScoreboardService service;

    @Inject
    public ProcessedTurnEventConsumer(final ScoreboardService service) {
        this.service = service;
    }

    @Incoming("processedturn-in")
    public void consume(final ProcessedTurnEvent evt) {
        service.handleProcessedTurnEvent(evt);
    }
}
