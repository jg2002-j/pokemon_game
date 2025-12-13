package com.clapped.pokemon.model.dto.move;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.clapped.pokemon.model.dto.NamedResource;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EffectChangeDto {

    @JsonProperty("effect_entries")
    private List<EffectEntryDto> effectEntries;

    @JsonProperty("version_group")
    private NamedResource versionGroup;
}

