package com.clapped.pokemon.model.move;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MoveFlag {
    DAMAGE("damage"), // Inflicts damage
    AILMENT("ailment"), // No damage; inflicts status ailment
    NET_GOOD_STATS("net-good-stats"), // No damage; lowers target’s stats or raises user’s stats
    HEAL("heal"), // No damage; heals the user
    DAMAGE_AILMENT("damage+ailment"), // Inflicts damage; inflicts status ailment
    SWAGGER("swagger"), // No damage; inflicts status ailment; raises target’s stats
    DAMAGE_LOWER("damage+lower"), // Inflicts damage; lowers target’s stats
    DAMAGE_RAISE("damage+raise"), // Inflicts damage; raises user’s stats
    DAMAGE_HEAL("damage+heal"), // Inflicts damage; absorbs damage done to heal the user
    OHKO("ohko"), // One-hit KO
    WHOLE_FIELD_EFFECT("whole-field-effect"), // Effect on the whole field
    FIELD_EFFECT("field-effect"), // Effect on one side of the field
    FORCE_SWITCH("force-switch"), // Forces target to switch out
    UNIQUE("unique"); // Unique effect

    private String name;
}
