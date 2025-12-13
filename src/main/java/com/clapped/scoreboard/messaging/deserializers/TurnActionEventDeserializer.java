package com.clapped.scoreboard.messaging.deserializers;

import com.clapped.main.messaging.events.TurnActionEvent;

public class TurnActionEventDeserializer extends JacksonDeserializer<TurnActionEvent> {
    public TurnActionEventDeserializer() {
        super(TurnActionEvent.class);
    }
}

