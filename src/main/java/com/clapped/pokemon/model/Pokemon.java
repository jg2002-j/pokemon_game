package com.clapped.pokemon.model;

import com.clapped.pokemon.model.pokemon.PokemonNature;
import com.clapped.pokemon.model.pokemon.PokemonStat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.EnumMap;
import java.util.List;

import static com.clapped.pokemon.model.pokemon.PokemonStat.HP;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {
    private long id;
    private String spriteLink;
    private String criesLink;
    private String name;
    private PokemonNature nature;
    private List<Type> types;
    private List<Move> moves; // .getCurrentPp()
    private EnumMap<PokemonStat, Integer> baseStats;

    private EnumMap<PokemonStat, Integer> currentStats;
    private Ailment currentAilment;
    @JsonIgnore
    private int ailmentRemainingTurns;

    private Move currentlyUsedMove;
    private int moveRemainingTurns; // how many turns are remaining on the multi-turn move

    public Pokemon(long id, String spriteLink, String criesLink, String name, PokemonNature nature, List<Type> types, List<Move> moves, EnumMap<PokemonStat, Integer> baseStats) {
        this.id = id;
        this.spriteLink = spriteLink;
        this.criesLink = criesLink;
        this.name = name;
        this.nature = nature;
        this.types = types;
        this.moves = moves;
        this.baseStats = baseStats;
        this.currentStats = baseStats;
        this.currentAilment = Ailment.NONE;
    }

    public int getCurrentHp() {
        return currentStats.get(HP);
    }

    public void setCurrentHp(final int hp) {
        currentStats.put(HP, hp);
    }

    public boolean isFainted() {
        return getCurrentHp() == 0;
    }

    public void inflictAilment(final Ailment ailment, int remainingTurns) {
        this.currentAilment = ailment;
        this.ailmentRemainingTurns = remainingTurns;
    }

    public boolean needsHealNonFaintedHp() {
        return !isFainted() && (getCurrentHp() < baseStats.get(HP));
    }

    public boolean needsHealNonFaintedStatus() {
        return !isFainted() && currentAilment != null && currentAilment != Ailment.NONE;
    }

    public String toPrettyString() {
        return name.toUpperCase();
    }
}
