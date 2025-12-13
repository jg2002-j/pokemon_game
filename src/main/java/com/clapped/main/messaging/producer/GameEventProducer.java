package com.clapped.main.messaging.producer;

import com.clapped.main.messaging.events.GameEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class GameEventProducer {

    @Inject
    @Channel("game")
    Emitter<GameEvent> emitter;

    public void sendGameEvent(final GameEvent evt) {
        log.info("Sending new {} event: {}", evt.getEventType(), evt);
        emitter.send(evt);
    }

}
