package com.clapped.scoreboard.messaging.deserializers;

import com.clapped.main.messaging.events.PlayerEvent;

public class PlayerEventDeserializer extends JacksonDeserializer<PlayerEvent> {
    public PlayerEventDeserializer() {
        super(PlayerEvent.class);
    }
}

