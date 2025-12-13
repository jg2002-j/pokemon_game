package com.clapped.pokemon.model.dto.pokemon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonDto {
    private long id;
    private String name;
    private Integer height;
    private Integer weight;
    private CriesDto cries;

    @JsonProperty("types")
    private List<TypeSlotDto> typeSlotDtos;

    private List<AbilitySlotDto> abilities;
    private List<PokemonStatDto> stats;
    private List<PokemonMove> moves;
    private SpritesDto sprites;
}
