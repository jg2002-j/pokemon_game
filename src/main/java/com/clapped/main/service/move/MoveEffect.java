package com.clapped.main.service.move;

import com.clapped.pokemon.model.Pokemon;

public interface MoveEffect {
    boolean canExecute(final Pokemon user, final Pokemon target);
    int calculateDamage(final Pokemon user, final Pokemon target);
    void applySecondaryEffects(final Pokemon user, final Pokemon target);
}
