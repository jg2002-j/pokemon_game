package com.clapped.pokemon.model.move;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MoveTarget {
    SPECIFIC_MOVE("specific-move"),
    SELECTED_POKEMON_ME_FIRST("selected-pokemon-me-first"),
    ALLY("ally"),
    USERS_FIELD("users-field"),
    USER_OR_ALLY("user-or-ally"),
    OPPONENTS_FIELD("opponents-field"),
    USER("user"),
    RANDOM_OPPONENT("random-opponent"),
    ALL_OTHER_POKEMON("all-other-pokemon"),
    SELECTED_POKEMON("selected-pokemon"),
    ALL_OPPONENTS("all-opponents"),
    ENTIRE_FIELD("entire-field"),
    USER_AND_ALLIES("user-and-allies"),
    ALL_POKEMON("all-pokemon"),
    ALL_ALLIES("all-allies"),
    FAINTING_POKEMON("fainting-pokemon");

    private String name;

    public static MoveTarget fromName(String name) {
        for (MoveTarget moveTarget : values()) {
            if (moveTarget.name.equalsIgnoreCase(name)) {
                return moveTarget;
            }
        }
        return null;
    }

}
