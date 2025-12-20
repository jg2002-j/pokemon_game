package com.clapped.pokemon.service;

import com.clapped.main.service.GameState;
import com.clapped.pokemon.model.pokemon.PokemonNature;
import com.clapped.pokemon.model.pokemon.PokemonStat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.EnumMap;

@ApplicationScoped
public class PokemonStatCalculator {

    private final GameState state;

    // Default assumptions
    private final int defaultIv = 31; // Gen III+
    private final int defaultEv = 0;
    private final int defaultDv = 15; // Gen I-II
    private final int defaultEvOld = 65535; // Gen I-II max EV

    @Inject
    public PokemonStatCalculator(final GameState state) {
        this.state = state;
    }

    private int calculateHPGen3Plus(int base, int level) {
        return ((2 * base + defaultIv + (defaultEv / 4)) * level) / 100 + level + 10;
    }

    private int calculateOtherStatGen3Plus(int base, int level, double natureModifier) {
        return (int) (((double) ((2 * base + defaultIv + (defaultEv / 4)) * level) / 100 + 5) * natureModifier);
    }

    private int calculateHPGen1_2(int base, int level) {
        return (((base + defaultDv) * 2 + (int) Math.sqrt(defaultEvOld) / 4) * level) / 100 + level + 10;
    }

    private int calculateOtherStatGen1_2(int base, int level) {
        return (((base + defaultDv) * 2 + (int) Math.sqrt(defaultEvOld) / 4) * level) / 100 + 5;
    }

    public EnumMap<PokemonStat, Integer> calculateStats(
            final EnumMap<PokemonStat, Integer> baseStats,
            final PokemonNature nature
    ) {
        EnumMap<PokemonStat, Integer> finalStats = new EnumMap<>(PokemonStat.class);

        for (PokemonStat stat : baseStats.keySet()) {
            int base = baseStats.get(stat);
            double natureModifier = 1.0;

            if (nature.getIncreasedStat() == stat) {
                natureModifier = 1.1;
            } else if (nature.getDecreasedStat() == stat) {
                natureModifier = 0.9;
            }

            if (stat == PokemonStat.HP) {
                if (state.getPokemonGen().getNumber() >= 3) {
                    finalStats.put(stat, calculateHPGen3Plus(base, state.getPokemonLevel()));
                } else {
                    finalStats.put(stat, calculateHPGen1_2(base, state.getPokemonLevel()));
                }
            } else if (stat == PokemonStat.ACCURACY || stat == PokemonStat.EVASION) {
                finalStats.put(stat, 100); // Placeholder for percentage-based stats
            } else {
                if (state.getPokemonGen().getNumber() >= 3) {
                    finalStats.put(stat, calculateOtherStatGen3Plus(base, state.getPokemonLevel(), natureModifier));
                } else {
                    finalStats.put(stat, calculateOtherStatGen1_2(base, state.getPokemonLevel()));
                }
            }
        }
        return finalStats;
    }
}
