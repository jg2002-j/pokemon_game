package com.clapped.scoreboard.messaging.deserializers;

import com.clapped.main.messaging.events.LobbyEvent;

public class LobbyEventsDeserializer extends JacksonDeserializer<LobbyEvent> {
    public LobbyEventsDeserializer() {
        super(LobbyEvent.class);
    }
}
