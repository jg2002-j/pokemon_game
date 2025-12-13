package com.clapped.pokemon.model.dto.pokemon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Pokemon cries - audio URLs for the latest and legacy cries
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CriesDto {

    /**
     * URL to the latest cry audio file
     */
    private String latest;

    /**
     * URL to the legacy cry audio file
     */
    private String legacy;
}

