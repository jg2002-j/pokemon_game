package com.clapped.main.messaging.events;

import com.clapped.main.model.Player;
import com.clapped.main.model.ProcessResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerEvent {
    private long timestamp;
    private Player player;
    private PlayerEvtType eventType;
    private ProcessResult result;
}
