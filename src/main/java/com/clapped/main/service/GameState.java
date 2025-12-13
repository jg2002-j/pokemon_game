package com.clapped.main.service;

import com.clapped.main.model.Player;
import com.clapped.pokemon.model.Generation;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@ApplicationScoped
public class GameState {

    @Setter
    private boolean useShowdownIcons = false;
    @Setter
    private int pokemonLevel = 50;
    @Setter
    private Generation pokemonGen = Generation.IV;

    private final Map<String, Player> players = new ConcurrentHashMap<>();

    public void addPlayer(String username, Player player) {
        players.put(username, player);
    }

    public Player removePlayer(String username) {
        return players.remove(username);
    }

    public Player getPlayer(String username) {
        return players.get(username);
    }

    public boolean hasPlayer(String username) {
        return players.containsKey(username);
    }

    public List<Player> getPlayersForTeam(int teamNumber) {
        LinkedList<Player> teamPlayers = new LinkedList<>();
        for (Player p : players.values()) {
            if (p.getTeamNum() == teamNumber) {
                teamPlayers.add(p);
            }
        }
        return teamPlayers;
    }

    public List<Player> getAllPlayers() {
        return new LinkedList<>(players.values());
    }
}