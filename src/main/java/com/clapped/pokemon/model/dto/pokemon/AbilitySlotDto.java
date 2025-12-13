package com.clapped.pokemon.model.dto.pokemon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.clapped.pokemon.model.dto.NamedResource;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbilitySlotDto {
    private NamedResource ability;

    @JsonProperty("is_hidden")
    private Boolean isHidden;

    private Integer slot;
}