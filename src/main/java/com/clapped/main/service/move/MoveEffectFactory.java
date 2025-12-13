package com.clapped.main.service.move;

import com.clapped.pokemon.model.Move;
import com.clapped.pokemon.model.move.MoveFlag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoveEffectFactory {

    private final HashMap<Long, List<MoveEffect>> customMoveEffects = new HashMap<>();
    private final HashMap<MoveFlag, MoveEffect> moveEffects = new HashMap<>();

    public List<MoveEffect> getEffectHandlers(final Move move) {
        // check if move has a custom handler
        List<MoveEffect> customEffects = customMoveEffects.get(move.getId());
        if (customEffects != null) {
            return customEffects;
        }

        List<MoveEffect> effects = new ArrayList<>();
        for (final MoveFlag flag : move.getMoveFlags()) {
            effects.add(moveEffects.get(flag));
        }
        return effects;
    }



}
