package com.clapped.main.messaging.producer;

import com.clapped.main.messaging.events.TurnActionEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class TurnActionEventProducer {

    @Inject
    @Channel("turn-action")
    Emitter<TurnActionEvent> turnEventemitter;

    public void sendTurnActionEvent(final TurnActionEvent evt) {
        log.info("Sending new turn-action {} event: {}", evt.getEventTypes(), evt);
        turnEventemitter.send(evt);
    }

}