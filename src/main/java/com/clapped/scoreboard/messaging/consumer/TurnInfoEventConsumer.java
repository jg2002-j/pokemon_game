package com.clapped.scoreboard.messaging.consumer;

import com.clapped.scoreboard.ws.EventsHandler;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

public class TurnInfoEventConsumer {

    private final EventsHandler eventsHandler;

    @Inject
    public TurnInfoEventConsumer(final EventsHandler eventsHandler) {
        this.eventsHandler = eventsHandler;
    }

    @Incoming("turn-info-in")
    public void consume(final String evt) {
//        log.info("Received {} event from Kafka: {}", evt.getEventType(), evt);
        eventsHandler.broadcast(evt);
    }

}
