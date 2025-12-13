package com.clapped.pokemon.model.pokemon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.clapped.pokemon.model.pokemon.PokemonStat.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PokemonNature {
    HARDY("hardy", null, null),
    BOLD("bold", DEFENSE, ATTACK),
    MODEST("modest", SPECIAL_ATTACK, ATTACK),
    CALM("calm", SPECIAL_DEFENSE, ATTACK),
    TIMID("timid", SPEED, ATTACK),
    LONELY("lonely", ATTACK, DEFENSE),
    DOCILE("docile", null, null),
    MILD("mild", SPECIAL_ATTACK, DEFENSE),
    GENTLE("gentle", SPECIAL_DEFENSE, DEFENSE),
    HASTY("hasty", SPEED, DEFENSE),
    ADAMANT("adamant", ATTACK, SPECIAL_ATTACK),
    IMPISH("impish", DEFENSE, SPECIAL_ATTACK),
    BASHFUL("bashful", null, null),
    CAREFUL("careful", SPECIAL_DEFENSE, SPECIAL_ATTACK),
    RASH("rash", SPECIAL_ATTACK, SPECIAL_DEFENSE),
    JOLLY("jolly", SPEED, SPECIAL_ATTACK),
    NAUGHTY("naughty", ATTACK, SPECIAL_DEFENSE),
    LAX("lax", DEFENSE, SPECIAL_DEFENSE),
    QUIRKY("quirky", null, null),
    NAIVE("naive", SPEED, SPECIAL_DEFENSE);

    private String name;
    private PokemonStat increasedStat;
    private PokemonStat decreasedStat;

}
