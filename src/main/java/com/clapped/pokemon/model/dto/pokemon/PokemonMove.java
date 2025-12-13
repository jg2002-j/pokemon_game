package com.clapped.pokemon.model.dto.pokemon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.clapped.pokemon.model.dto.NamedResource;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonMove {
    private NamedResource move;

    @JsonProperty("version_group_details")
    private List<VersionGroupDetail> versionGroupDetails;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VersionGroupDetail {
        @JsonProperty("level_learned_at")
        private Integer levelLearnedAt;

        // level-up, machine
        @JsonProperty("move_learn_method")
        private NamedResource moveLearnMethod;

        @JsonProperty("version_group")
        private NamedResource versionGroup;

        private Integer order;
    }
}

