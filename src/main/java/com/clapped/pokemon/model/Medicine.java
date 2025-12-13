package com.clapped.pokemon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.clapped.pokemon.model.Ailment.*;
import static com.clapped.pokemon.model.pokemon.PokemonStat.HP;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Medicine {
    POTION(20, null),
    SUPER_POTION(50, null),
    HYPER_POTION(200, null),
    MAX_POTION(-2, null),
    FULL_RESTORE(-2, List.of(BURN, PARALYSIS, POISON, FREEZE, SLEEP)),

    ANTIDOTE(0, List.of(POISON)),
    PARALYZE_HEAL(0, List.of(PARALYSIS)),
    BURN_HEAL(0, List.of(BURN)),
    ICE_HEAL(0, List.of(FREEZE)),
    AWAKENING(0, List.of(SLEEP)),
    FULL_HEAL(0, List.of(BURN, PARALYSIS, POISON, FREEZE, SLEEP)),

    REVIVE(-1, null),
    MAX_REVIVE(-2, null);

    private int maxHealHpAmount;
    private List<Ailment> healStatus;

    public int getHealPokemonHpAmount(final Pokemon pokemon) {
        final int pokemonMaxHp = pokemon.getBaseStats().get(HP);
        final int pokemonPrevHp = pokemon.getCurrentHp();
        int actualHealAmount;
        actualHealAmount = switch (maxHealHpAmount) {
            case -1 -> pokemonMaxHp / 2;
            case -2 -> pokemonMaxHp;
            default -> maxHealHpAmount;
        };
        return Math.min(actualHealAmount, pokemonMaxHp - pokemonPrevHp);
    }

    public boolean hasEffectOnPokemon(final Pokemon p) {
        final boolean willHealHp = p.needsHealNonFaintedHp() && canHealNonFaintedHp();
        final boolean willHealStatus = p.needsHealNonFaintedStatus() && canHealNonFaintedStatus(p.getCurrentAilment());
        return p.isFainted() ? canRevive() : (willHealStatus || willHealHp);
    }

    public boolean canRevive() {
        return this == REVIVE || this == MAX_REVIVE;
    }

    public boolean canHealNonFaintedHp() {
        return maxHealHpAmount != 0 && !canRevive();
    }

    public boolean canHealNonFaintedStatus(final Ailment status) {
        return healStatus != null && healStatus.contains(status);
    }

}
