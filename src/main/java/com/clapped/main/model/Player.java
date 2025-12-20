package com.clapped.main.model;

import com.clapped.pokemon.model.Pokemon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.Objects;

@NoArgsConstructor
@Accessors(chain = true)
public class Player {
    @Getter
    @Setter
    private String username;
@Getter
@Setter
private String avatarUrl;

    @Getter
    @Setter
    private int teamNum;

    @Getter
    private LinkedList<Pokemon> pokemonTeam;
    @Setter
    @Getter
    private int activePokemonIndex;

    public Player(final String username, final String avatarUrl, final int teamNum, final LinkedList<Pokemon> pokemonTeam) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.teamNum = teamNum;
        setPokemonTeam(pokemonTeam);
        activePokemonIndex = 0;
    }

    public Pokemon getPokemon() {
        return pokemonTeam.get(activePokemonIndex);
    }

    public void setPokemonTeam(final LinkedList<Pokemon> pokemonTeam) {
        if (pokemonTeam != null && pokemonTeam.size() > 6) {
            throw new IllegalArgumentException("Maximum team size is 6");
        }
        this.pokemonTeam = pokemonTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(username, player.username);
    }
}
