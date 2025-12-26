package com.clapped.main.messaging.producer;

import com.clapped.main.messaging.events.TurnStartEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class TurnStartEventProducer {
    @Inject
    @Channel("turnstart")
    Emitter<TurnStartEvent> emitter;

    public void sendTurnStartEvent(final TurnStartEvent evt) {
        log.info("Sending new turn start event: {}", evt);
        emitter.send(evt);
    }
}
