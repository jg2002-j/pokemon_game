package com.clapped.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitAction implements PlayerAction {

    private Player player;

    @Override
    public String getTurnUser() {
        return player.getUsername();
    }

    @Override
    public String toPrettyString() {
        return String.format(
                "%s's %s is already using %s.",
                player.getUsername(),
                player.getPokemon().getName().toUpperCase(),
                player.getPokemon().getCurrentlyUsedMove().getName()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WaitAction waitTurn = (WaitAction) o;
        return Objects.equals(player, waitTurn.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
}

