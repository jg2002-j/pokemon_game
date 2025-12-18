package com.clapped.scoreboard;

import com.clapped.main.model.ActionType;
import com.clapped.main.model.Player;
import com.clapped.pokemon.model.Generation;
import com.clapped.scoreboard.ws.EventsHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ScoreboardService {

    private Generation pkmnGen;
    private int pkmnLvl;

    private List<Player> players;

    private int turnNum;
    private Map<String, List<ActionType>> playerTurnOptions;

    private final EventsHandler eventsHandler;

    @Inject
    public ScoreboardService(final EventsHandler eventsHandler) {
        this.eventsHandler = eventsHandler;
    }

    


}
