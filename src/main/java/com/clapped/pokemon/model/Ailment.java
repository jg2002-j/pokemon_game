package com.clapped.pokemon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Ailment {
    NONE("none"),
    PARALYSIS("paralysis"),
    SLEEP("sleep"),
    FREEZE("freeze"),
    BURN("burn"),
    POISON("poison"),

    CONFUSION("confusion"),
    INFATUATION("infatuation"),

    TRAP("trap"),
    NIGHTMARE("nightmare"),
    TORMENT("torment"),
    DISABLE("disable"),
    YAWN("yawn"),
    HEAL_BLOCK("heal-block"),
    NO_TYPE_IMMUNITY("no-type-immunity"),
    LEECH_SEED("leech-seed"),
    EMBARGO("embargo"),
    PERISH_SONG("perish-song"),
    INGRAIN("ingrain"),
    SILENCE("silence"),
    TAR_SHOT("tar-shot");

    private String name;

    public static Ailment fromName(String name) {
        for (Ailment ailment : values()) {
            if (ailment.name.equalsIgnoreCase(name)) {
                return ailment;
            }
        }
        return NONE;
    }
}
