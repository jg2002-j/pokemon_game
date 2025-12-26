package com.clapped.scoreboard.ws;

import com.clapped.main.model.ActionType;
import com.clapped.main.model.Player;
import com.clapped.scoreboard.ScoreboardState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateSnapshot {
    private int pkmnGen;
    private int pkmnLvl;
    private int turnNum;
    private Map<String, Player> players;
    private Map<String, List<ActionType>> playerTurnOptions;
    private Map<String, String> usernamesAndActions;

    public static StateSnapshot from(final ScoreboardState state) {
        final Map<String, Player> players;
        if (state.getPlayers() == null) {
            players = Map.of();
        } else {
            // Copy into a regular map first to avoid CME if the backing map is being mutated.
            players = Map.copyOf(new HashMap<>(state.getPlayers()));
        }

        final Map<String, List<ActionType>> turnOptions;
        if (state.getPlayerTurnOptions() == null) {
            turnOptions = Map.of();
        } else {
            // Copy map + copy lists so clients can't mutate server state and Jackson won't race with mutations.
            final Map<String, List<ActionType>> tmp = new HashMap<>();
            state.getPlayerTurnOptions().forEach((k, v) -> tmp.put(k, v == null ? List.of() : List.copyOf(v)));
            turnOptions = Map.copyOf(tmp);
        }

        final Map<String, String> usernamesAndActions;
        if (state.getUsernamesAndActions() == null) {
            usernamesAndActions = Map.of();
        } else {
            usernamesAndActions = Map.copyOf(new HashMap<>(state.getUsernamesAndActions()));
        }

        return new StateSnapshot(
                state.getPkmnGen(),
                state.getPkmnLvl(),
                state.getTurnNum(),
                players,
                turnOptions,
                usernamesAndActions
        );
    }
}
