package com.clapped.pokemon.model.dto.generation;

import com.clapped.pokemon.model.dto.NamedResource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationDto {
    private String name;
    @JsonProperty("pokemon_species")
    private List<NamedResource> pokemonSpecies;
}
