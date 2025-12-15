package com.clapped.pokemon.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public enum Type {
    NORMAL("normal"),
    FIRE("fire"),
    WATER("water"),
    ELECTRIC("electric"),
    GRASS("grass"),
    ICE("ice"),
    FIGHTING("fighting"),
    POISON("poison"),
    GROUND("ground"),
    FLYING("flying"),
    PSYCHIC("psychic"),
    BUG("bug"),
    ROCK("rock"),
    GHOST("ghost"),
    DRAGON("dragon"),
    DARK("dark"),
    STEEL("steel"),
    FAIRY("fairy"),
    UNKNOWN("unknown");

    @Getter
    private final String name;
    @Getter @Setter
    private ImageLinks imgLinks;
    @Getter @Setter
    private List<Type> superEffectiveAgainst;
    @Getter @Setter
    private List<Type> ineffectiveAgainst;
    @Getter @Setter
    private List<Type> immuneAgainst;

    Type(final String name) {
        this.name = name;
    }

    public static Type fromName(String name) {
        for (Type type : values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public boolean isSuperEffectiveAgainst(final Type type) {
        return superEffectiveAgainst.contains(type);
    }

    public boolean isIneffectiveAgainst(final Type type) {
        return ineffectiveAgainst.contains(type);
    }

    public boolean isImmuneAgainst(final Type type) {
        return immuneAgainst.contains(type);
    }

}
