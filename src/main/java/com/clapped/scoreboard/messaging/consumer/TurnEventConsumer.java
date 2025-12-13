package com.clapped.scoreboard.messaging.consumer;

import com.clapped.scoreboard.ws.EventsHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Slf4j
@ApplicationScoped
public class TurnEventConsumer {

    private final EventsHandler eventsHandler;

    @Inject
    public TurnEventConsumer(final EventsHandler eventsHandler) {
        this.eventsHandler = eventsHandler;
    }

    @Incoming("turn-action-in")
    public void consume(final String evt) {
//        log.info("Received new turn-action {} event from Kafka: {}", evt.getEventTypes(), evt);
        eventsHandler.broadcast(evt);
    }

}