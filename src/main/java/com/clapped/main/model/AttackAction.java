package com.clapped.main.model;

import com.clapped.pokemon.model.Move;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttackAction implements PlayerAction {

    private Player attackerPlayer;
    private Move usedMove;
    private Player targetPlayer;

    @Override
    public String getTurnUser() {
        return attackerPlayer.getUsername();
    }

    @Override
    public String toPrettyString() {
        return String.format(
                "%s's %s will use %s on %s's %s.",
                attackerPlayer.getUsername(),
                attackerPlayer.getPokemon().getName().toUpperCase(),
                usedMove.getName(),
                targetPlayer.getUsername(),
                targetPlayer.getPokemon().getName().toUpperCase()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AttackAction that = (AttackAction) o;
        return Objects.equals(attackerPlayer, that.attackerPlayer) && Objects.equals(targetPlayer, that.targetPlayer) && Objects.equals(usedMove, that.usedMove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attackerPlayer, targetPlayer, usedMove);
    }
}
