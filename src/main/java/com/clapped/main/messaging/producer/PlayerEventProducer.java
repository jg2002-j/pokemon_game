package com.clapped.main.messaging.producer;

import com.clapped.main.messaging.events.PlayerEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class PlayerEventProducer {

    @Inject
    @Channel("player")
    Emitter<PlayerEvent> emitter;

    public void sendPlayerEvent(final PlayerEvent evt) {
        log.info("Sending new {} event: {}", evt.getEventType(), evt);
        emitter.send(evt);
    }
}