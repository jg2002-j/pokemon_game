package com.clapped.scoreboard.messaging.deserializers;

import com.clapped.main.messaging.events.GameEvent;

public class GameEventDeserializer extends JacksonDeserializer<GameEvent> {
    public GameEventDeserializer() {
        super(GameEvent.class);
    }
}

