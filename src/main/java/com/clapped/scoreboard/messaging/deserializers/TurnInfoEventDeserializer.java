package com.clapped.scoreboard.messaging.deserializers;

import com.clapped.main.messaging.events.TurnInfoEvent;

public class TurnInfoEventDeserializer extends JacksonDeserializer<TurnInfoEvent> {
    public TurnInfoEventDeserializer() {
        super(TurnInfoEvent.class);
    }
}

