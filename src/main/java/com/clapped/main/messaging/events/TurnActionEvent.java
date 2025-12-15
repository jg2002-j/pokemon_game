package com.clapped.main.messaging.events;

import com.clapped.main.model.Player;
import com.clapped.main.model.ProcessResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnActionEvent {
    private long timestamp;
    private EventType eventType;
    private List<TurnActionEvtType> turnActionEvtTypes;
    private Player affectedPlayer;
    private ProcessResult result;
}
