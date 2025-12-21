package com.clapped.scoreboard;

import com.clapped.main.model.ActionType;
import com.clapped.main.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
public class ScoreboardState {
    private int pkmnGen;
    private int pkmnLvl;
    private int turnNum;
    private Map<String, Player> players;
    private Map<String, List<ActionType>> playerTurnOptions;

    public ScoreboardState() {
        players = new ConcurrentHashMap<>();
        playerTurnOptions = new ConcurrentHashMap<>();
    }
}
