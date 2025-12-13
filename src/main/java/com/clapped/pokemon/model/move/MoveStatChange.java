package com.clapped.pokemon.model.move;

import com.clapped.pokemon.model.pokemon.PokemonStat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveStatChange {
    private PokemonStat pokemonStat;
    private Integer change;
    private Integer chance; // the chance (percentage) this stat change occurs
}
