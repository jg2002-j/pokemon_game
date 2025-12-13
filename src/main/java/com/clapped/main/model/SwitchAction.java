package com.clapped.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwitchAction implements PlayerAction {

    private Player player;
    private int newPokemonIndex;

    @Override
    public String getTurnUser() {
        return player.getUsername();
    }

    @Override
    public String toPrettyString() {
        return String.format(
                "%s will send out %s.",
                player.getUsername(),
                player.getPokemonTeam().get(newPokemonIndex).getName().toUpperCase()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SwitchAction that = (SwitchAction) o;
        return newPokemonIndex == that.newPokemonIndex && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, newPokemonIndex);
    }
}

