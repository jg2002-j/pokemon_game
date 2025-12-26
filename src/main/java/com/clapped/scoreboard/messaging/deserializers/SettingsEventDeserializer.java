package com.clapped.scoreboard.messaging.deserializers;

import com.clapped.main.messaging.events.SettingsEvent;

public class SettingsEventDeserializer extends JacksonDeserializer<SettingsEvent> {
    public SettingsEventDeserializer() {
        super(SettingsEvent.class);
    }
}
