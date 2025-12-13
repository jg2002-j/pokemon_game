package com.clapped.pokemon.model.dto.move;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.clapped.pokemon.model.dto.NamedResource;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveDto {

    private long id;
    private String rawName;
    private Integer accuracy;
    private Integer pp;
    private Integer priority;
    private Integer power;
    private NamedResource target;
    private NamedResource type;
    @JsonProperty("damage_class") private NamedResource damageClass;
    @JsonProperty("effect_entries") private List<EffectEntryDto> effectEntries;
    @JsonProperty("effect_chance") private Integer effectChance;
    private MoveMetaDto meta;

    @JsonProperty("stat_changes")
    private List<MoveStatChangeDto> statChanges;

    private List<NameDto> names;

    // PREVIOUS GEN VALUES

    @JsonProperty("past_values")
    private List<PastValueDto> pastValueDtos;

    @JsonProperty("effect_changes")
    private List<EffectChangeDto> effectChangeDtos;

    public String getPrettyName() {
        return names.stream()
                .filter(n -> "en".equals(n.getLanguage().getName()))
                .findFirst()
                .map(NameDto::getName)
                .orElse(rawName);
    }


}
