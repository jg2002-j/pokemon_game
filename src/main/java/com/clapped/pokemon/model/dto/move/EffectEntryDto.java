package com.clapped.pokemon.model.dto.move;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EffectEntryDto {
    private String effect;

    @JsonProperty("short_effect")
    private String shortEffect;
}
