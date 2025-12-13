package com.clapped.pokemon.model.move;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MoveDamageClass {
    PHYSICAL("physical"),
    SPECIAL("special"),
    STATUS("status");

    private String name;

    public static MoveDamageClass fromName(String name) {
        for (MoveDamageClass damageClass : values()) {
            if (damageClass.name.equalsIgnoreCase(name)) {
                return damageClass;
            }
        }
        return null;
    }

}
