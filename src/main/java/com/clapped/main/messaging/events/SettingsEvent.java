package com.clapped.main.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsEvent {
    private int generation;
    private int level;
    private LinkedList<String> logMsgs;
}
