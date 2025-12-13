package com.clapped.pokemon.model.dto.pokemon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.clapped.pokemon.model.dto.NamedResource;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeSlotDto {
    private Integer slot;
    private NamedResource type;
}
