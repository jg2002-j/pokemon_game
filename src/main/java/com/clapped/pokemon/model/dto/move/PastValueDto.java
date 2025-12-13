package com.clapped.pokemon.model.dto.move;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.clapped.pokemon.model.dto.NamedResource;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PastValueDto {

    private Integer accuracy;
    private Integer power;
    private Integer pp;

    @JsonProperty("effect_chance")
    private Integer effectChance;

    @JsonProperty("effect_entries")
    private List<EffectEntryDto> effectEntries;

    private NamedResource type;

    @JsonProperty("version_group")
    private NamedResource versionGroup;
}

