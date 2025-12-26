package com.clapped.main.messaging.producer;

import com.clapped.main.messaging.events.ProcessedTurnEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class ProcessedTurnEventEmitter {
    @Inject
    @Channel("processedturn")
    Emitter<ProcessedTurnEvent> emitter;

    public void sendProcessedTurnEvent(final ProcessedTurnEvent evt) {
        log.info("Sending new processed turn event: {}", evt);
        emitter.send(evt);
    }
}
