package com.clapped.pokemon.model.dto.species;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.clapped.pokemon.model.dto.NamedResource;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeciesDto {
    private NamedResource generation;
}
