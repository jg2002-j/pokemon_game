package com.clapped.pokemon.model.dto.move;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.clapped.pokemon.model.dto.NamedResource;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveStatChangeDto {
    private Integer change;
    private NamedResource stat;
}

