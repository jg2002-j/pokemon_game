package com.clapped.scoreboard.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsMessageWrapper {
    private int version;
    private WsMsgType type;
    private StateSnapshot payload;
    private List<String> logMsgs;
}