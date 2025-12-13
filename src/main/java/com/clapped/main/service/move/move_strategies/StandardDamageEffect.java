package com.clapped.main.service.move.move_strategies;

import com.clapped.main.service.move.MoveEffect;
import com.clapped.pokemon.model.Ailment;
import com.clapped.pokemon.model.Pokemon;

public class StandardDamageEffect implements MoveEffect {
    @Override
    public boolean canExecute(Pokemon user, Pokemon target) {
        if (user.isFainted()) return false;
        if (target.isFainted()) return false;
        if (user.getCurrentAilment() == Ailment.SLEEP) return false;
        return true;
    }

    @Override
    public int calculateDamage(Pokemon user, Pokemon target) {
        return 0;
    }

    @Override
    public void applySecondaryEffects(Pokemon user, Pokemon target) {
        // none
    }
}
