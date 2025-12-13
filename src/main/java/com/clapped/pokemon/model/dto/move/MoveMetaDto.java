package com.clapped.pokemon.model.dto.move;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.clapped.pokemon.model.dto.NamedResource;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveMetaDto {

    private NamedResource ailment;

    @JsonProperty("ailment_chance")
    private Integer ailmentChance;

    private NamedResource category;

    @JsonProperty("crit_rate")
    private Integer critRate;

    private Integer drain;

    @JsonProperty("flinch_chance")
    private Integer flinchChance;

    private Integer healing;

    @JsonProperty("max_hits")
    private Integer maxHits;

    @JsonProperty("max_turns")
    private Integer maxTurns;

    @JsonProperty("min_hits")
    private Integer minHits;

    @JsonProperty("min_turns")
    private Integer minTurns;

    @JsonProperty("stat_chance")
    private Integer statChance;
}

