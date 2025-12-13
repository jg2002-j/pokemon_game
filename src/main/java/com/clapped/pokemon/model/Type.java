package com.clapped.pokemon.model;

import lombok.Getter;

import java.util.List;

public enum Type {
    NORMAL("normal", "assets/images/type/NormalIC_FRLG.png"),
    FIRE("fire", "assets/images/type/FireIC_FRLG.png"),
    WATER("water", "assets/images/type/WaterIC_FRLG.png"),
    ELECTRIC("electric", "assets/images/type/ElectricIC_FRLG.png"),
    GRASS("grass", "assets/images/type/GrassIC_FRLG.png"),
    ICE("ice", "assets/images/type/IceIC_FRLG.png"),
    FIGHTING("fighting", "assets/images/type/FightingIC_FRLG.png"),
    POISON("poison", "assets/images/type/PoisonIC_FRLG.png"),
    GROUND("ground", "assets/images/type/GroundIC_FRLG.png"),
    FLYING("flying", "assets/images/type/FlyingIC_FRLG.png"),
    PSYCHIC("psychic", "assets/images/type/PsychicIC_FRLG.png"),
    BUG("bug", "assets/images/type/BugIC_FRLG.png"),
    ROCK("rock", "assets/images/type/RockIC_FRLG.png"),
    GHOST("ghost", "assets/images/type/GhostIC_FRLG.png"),
    DRAGON("dragon", "assets/images/type/DragonIC_FRLG.png"),
    DARK("dark", "assets/images/type/DarkIC_FRLG.png"),
    STEEL("steel", "assets/images/type/SteelIC_FRLG.png"),
    FAIRY("fairy", ""),
    UNKNOWN("unknown", "assets/images/type/Unknown_FRLG.png");

    @Getter
    private final String name;
    @Getter
    private final String imagePath;
    private List<Type> superEffectiveAgainst;
    private List<Type> ineffectiveAgainst;
    private List<Type> immuneAgainst;

    Type(final String name, final String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public static Type fromName(String name) {
        for (Type type : values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    static {
        NORMAL.superEffectiveAgainst = List.of();
        NORMAL.ineffectiveAgainst = List.of(ROCK, STEEL);
        NORMAL.immuneAgainst = List.of(GHOST);

        FIRE.superEffectiveAgainst = List.of(GRASS, ICE, BUG, STEEL);
        FIRE.ineffectiveAgainst = List.of(FIRE, WATER, ROCK, DRAGON);
        FIRE.immuneAgainst = List.of();

        WATER.superEffectiveAgainst = List.of(FIRE, GROUND, ROCK);
        WATER.ineffectiveAgainst = List.of(WATER, GRASS, DRAGON);
        WATER.immuneAgainst = List.of();

        ELECTRIC.superEffectiveAgainst = List.of(WATER, FLYING);
        ELECTRIC.ineffectiveAgainst = List.of(ELECTRIC, GRASS, DRAGON);
        ELECTRIC.immuneAgainst = List.of(GROUND);

        GRASS.superEffectiveAgainst = List.of(WATER, GROUND, ROCK);
        GRASS.ineffectiveAgainst = List.of(FIRE, GRASS, POISON, FLYING, BUG, DRAGON, STEEL);
        GRASS.immuneAgainst = List.of();

        ICE.superEffectiveAgainst = List.of(GRASS, GROUND, FLYING, DRAGON);
        ICE.ineffectiveAgainst = List.of(FIRE, WATER, ICE, STEEL);
        ICE.immuneAgainst = List.of();

        FIGHTING.superEffectiveAgainst = List.of(NORMAL, ICE, ROCK, DARK, STEEL);
        FIGHTING.ineffectiveAgainst = List.of(POISON, FLYING, PSYCHIC, BUG);
        FIGHTING.immuneAgainst = List.of(GHOST);

        POISON.superEffectiveAgainst = List.of(GRASS);
        POISON.ineffectiveAgainst = List.of(POISON, GROUND, ROCK, GHOST);
        POISON.immuneAgainst = List.of(STEEL);

        GROUND.superEffectiveAgainst = List.of(FIRE, ELECTRIC, POISON, ROCK, STEEL);
        GROUND.ineffectiveAgainst = List.of(GRASS, BUG);
        GROUND.immuneAgainst = List.of(FLYING);

        FLYING.superEffectiveAgainst = List.of(GRASS, FIGHTING, BUG);
        FLYING.ineffectiveAgainst = List.of(ELECTRIC, ROCK, STEEL);
        FLYING.immuneAgainst = List.of();

        PSYCHIC.superEffectiveAgainst = List.of(FIGHTING, POISON);
        PSYCHIC.ineffectiveAgainst = List.of(PSYCHIC, STEEL);
        PSYCHIC.immuneAgainst = List.of(DARK);

        BUG.superEffectiveAgainst = List.of(GRASS, PSYCHIC, DARK);
        BUG.ineffectiveAgainst = List.of(FIRE, FIGHTING, POISON, FLYING, GHOST, STEEL);
        BUG.immuneAgainst = List.of();

        ROCK.superEffectiveAgainst = List.of(FIRE, ICE, FLYING, BUG);
        ROCK.ineffectiveAgainst = List.of(FIGHTING, GROUND, STEEL);
        ROCK.immuneAgainst = List.of();

        GHOST.superEffectiveAgainst = List.of(PSYCHIC, GHOST);
        GHOST.ineffectiveAgainst = List.of(DARK);
        GHOST.immuneAgainst = List.of(NORMAL);

        DRAGON.superEffectiveAgainst = List.of(DRAGON);
        DRAGON.ineffectiveAgainst = List.of(STEEL);
        DRAGON.immuneAgainst = List.of();

        DARK.superEffectiveAgainst = List.of(PSYCHIC, GHOST);
        DARK.ineffectiveAgainst = List.of(FIGHTING, DARK, STEEL);
        DARK.immuneAgainst = List.of();

        STEEL.superEffectiveAgainst = List.of(ICE, ROCK);
        STEEL.ineffectiveAgainst = List.of(FIRE, WATER, ELECTRIC, STEEL);
        STEEL.immuneAgainst = List.of();

        UNKNOWN.superEffectiveAgainst = List.of();
        UNKNOWN.ineffectiveAgainst = List.of();
        UNKNOWN.immuneAgainst = List.of();
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
