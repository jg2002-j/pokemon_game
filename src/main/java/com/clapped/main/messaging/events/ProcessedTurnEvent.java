package com.clapped.main.messaging.events;

import com.clapped.main.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedTurnEvent {
    private int turnNum;
    private List<Player> changedPlayers;
    private LinkedList<String> logMsgs;
}
