package com.clapped.pokemon.model.dto.type;

import com.clapped.pokemon.model.dto.NamedResource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeDto {

    private int id;
    private String name;
    @JsonProperty("damage_relations") private DamageRelations damageRelations;
    private Map<String, Map<String, Sprite>> sprites;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DamageRelations {
        @JsonProperty("double_damage_to") private List<NamedResource> doubleDamageTo;
        @JsonProperty("half_damage_to") private List<NamedResource> halfDamageTo;
        @JsonProperty("no_damage_to") private List<NamedResource> noDamageTo;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sprite {
        @JsonProperty("name_icon") private String imgLink;
    }


}
