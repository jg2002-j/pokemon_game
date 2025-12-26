package com.clapped.main.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnQueueEvent {
    private int turnNum;
    private Map<String, String> usernamesAndActions;
    private List<String> usernamesWithoutActions;
    private String logMsg;
}
