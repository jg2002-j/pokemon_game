package com.clapped.scoreboard.messaging.deserializers;

import com.clapped.main.messaging.events.TurnQueueEvent;

public class TurnQueueEventDeserializer extends JacksonDeserializer<TurnQueueEvent> {
    public TurnQueueEventDeserializer() {
        super(TurnQueueEvent.class);
    }
}
