package com.clapped.main.messaging.events;

import com.clapped.main.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LobbyEvent {
    private Player player;
    private JoinLeave joinLeave;
    private String logMsg;
}
