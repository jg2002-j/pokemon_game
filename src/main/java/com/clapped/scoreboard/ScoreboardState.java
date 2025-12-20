package com.clapped.scoreboard;

import com.clapped.main.model.ActionType;
import com.clapped.main.model.Player;
import com.clapped.pokemon.model.Generation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ScoreboardState {
    private Generation pkmnGen;
    private int pkmnLvl;
    private int turnNum;
    private Map<String, Player> players;
    private Map<String, List<ActionType>> playerTurnOptions;

    public ScoreboardState() {
        players = new ConcurrentHashMap<>();
        playerTurnOptions = new ConcurrentHashMap<>();
    }
}
