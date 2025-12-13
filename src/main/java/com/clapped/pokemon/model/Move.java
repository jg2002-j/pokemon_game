package com.clapped.pokemon.model;

import com.clapped.pokemon.model.move.MoveDamageClass;
import com.clapped.pokemon.model.move.MoveFlag;
import com.clapped.pokemon.model.move.MoveStatChange;
import com.clapped.pokemon.model.move.MoveTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Move {

    private long id;
    private String name;

    private Integer accuracy;
    private Integer currentPp;
    private Integer basePp;
    private Integer priority;
    private Integer power;
    private Type type; // normal, fighting, flying, etc.
    private MoveDamageClass damageClass; // physical, special, status

    private MoveTarget target;

    private Integer drain; // percentage of damageDealt
    private Integer healing; // percentage of maxHP
    private Integer flinchChance;

    private Integer maxHits;
    private Integer minHits;

    private Integer maxTurns;
    private Integer minTurns;

    private Ailment inflictsAilment; // ailment the move inflicts
    private Integer ailmentChance; // the chance it inflicts it
    private List<MoveStatChange> changesStats; // stats the move changes (includes stat, change amount, and chance)

    private List<MoveFlag> moveFlags; // other effects, conditions, etc. the move has

}
