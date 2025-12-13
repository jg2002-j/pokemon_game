package com.clapped.scoreboard.messaging.consumer;

import com.clapped.scoreboard.ws.EventsHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Slf4j
@ApplicationScoped
public class GameEventConsumer {

    private final EventsHandler eventsHandler;

    @Inject
    public GameEventConsumer(final EventsHandler eventsHandler) {
        this.eventsHandler = eventsHandler;
    }

    @Incoming("game-in")
    public void consume(final String evt) {
//        log.info("Received {} event from Kafka: {}", evt.getEventType(), evt);
        eventsHandler.broadcast(evt);
    }

}