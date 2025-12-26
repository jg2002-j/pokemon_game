package com.clapped.main.messaging.events;

import com.clapped.main.model.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnStartEvent {
    private int turnNum;
    private Map<String, List<ActionType>> playerTurnOptions;
    private String logMsg;
}
