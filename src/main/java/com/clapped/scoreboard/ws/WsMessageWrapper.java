package com.clapped.scoreboard.ws;

import com.clapped.main.model.ProcessResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsMessageWrapper {
    private int version;
    private WsMsgType type;
    private StateDto payload;
    private ProcessResult result;
}