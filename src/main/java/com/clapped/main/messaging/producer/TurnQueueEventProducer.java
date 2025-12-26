package com.clapped.main.messaging.producer;

import com.clapped.main.messaging.events.TurnQueueEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class TurnQueueEventProducer {
    @Inject
    @Channel("turnqueue")
    Emitter<TurnQueueEvent> emitter;

    public void sendTurnQueueEvent(final TurnQueueEvent evt) {
        log.info("Sending new turn queue event: {}", evt);
        emitter.send(evt);
    }
}
