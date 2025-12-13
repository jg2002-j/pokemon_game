package com.clapped.main.messaging.events;

import com.clapped.main.model.ActionType;
import com.clapped.main.model.ProcessResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnInfoEvent {
    private long timestamp;
    private Map<String, List<ActionType>> playerActionOptions;
    private ProcessResult result;
}
