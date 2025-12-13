package com.clapped.main.messaging.producer;

import com.clapped.main.messaging.events.TurnInfoEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class TurnInfoEventProducer {

    @Inject
    @Channel("turn-info")
    Emitter<TurnInfoEvent> turnInfoEventEmitter;

    public void sendTurnInfoEvent(final TurnInfoEvent evt) {
        log.info("Sending new turn-info event: {}", evt);
        turnInfoEventEmitter.send(evt);
    }

}