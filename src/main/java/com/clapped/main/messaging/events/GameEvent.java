package com.clapped.main.messaging.events;

import com.clapped.main.model.ProcessResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameEvent {
    private long timestamp;
    private GameEvtType eventType;
    private int newVal;
    private ProcessResult result;
}
