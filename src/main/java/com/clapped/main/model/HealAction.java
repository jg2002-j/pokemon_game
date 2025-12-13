package com.clapped.main.model;

import com.clapped.pokemon.model.Medicine;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class HealAction implements PlayerAction {
    private Player player;
    private Medicine medicine;

    public HealAction(final Player player, final Medicine medicine) {
        this.player = player;
        this.medicine = medicine;
    }

    @Override
    public String getTurnUser() {
        return player.getUsername();
    }

    @Override
    public String toPrettyString() {
        return String.format(
                "%s will use %s on %s.",
                player.getUsername(),
                medicine,
                player.getPokemon().getName().toUpperCase()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HealAction healTurn = (HealAction) o;
        return Objects.equals(player, healTurn.player) && medicine == healTurn.medicine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, medicine);
    }
}
