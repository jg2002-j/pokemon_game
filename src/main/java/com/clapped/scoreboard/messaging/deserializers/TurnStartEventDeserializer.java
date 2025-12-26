package com.clapped.scoreboard.messaging.deserializers;

import com.clapped.main.messaging.events.TurnStartEvent;

public class TurnStartEventDeserializer extends JacksonDeserializer<TurnStartEvent> {
    public TurnStartEventDeserializer() {
        super(TurnStartEvent.class);
    }
}
