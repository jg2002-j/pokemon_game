package com.clapped.scoreboard.messaging.deserializers;

import com.clapped.main.messaging.events.ProcessedTurnEvent;

public class ProcessedTurnEventDeserializer extends JacksonDeserializer<ProcessedTurnEvent> {
    public ProcessedTurnEventDeserializer() {
        super(ProcessedTurnEvent.class);
    }
}
