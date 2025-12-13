package com.clapped.pokemon.model.pokemon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PokemonStat {
    HP("hp"),
    ATTACK("attack"),
    DEFENSE("defense"),
    SPECIAL_ATTACK("special-attack"),
    SPECIAL_DEFENSE("special-defense"),
    SPEED("speed"),
    ACCURACY("accuracy"),
    EVASION("evasion");

    private String name;

    public static PokemonStat fromName(String name) {
        for (PokemonStat stat : values()) {
            if (stat.name.equalsIgnoreCase(name)) {
                return stat;
            }
        }
        return null;
    }
}
