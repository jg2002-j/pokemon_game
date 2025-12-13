package com.clapped.main.service;

import com.clapped.main.model.*;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BattleEngine {

    public ProcessResult switchout(final SwitchAction action) {
        // TODO: Implement switch logic
        // Swap player's active Pokémon to newPokemonIndex
        // Send TurnEvent
        return null;
    }

    public ProcessResult heal(final HealAction action) {
        // TODO: Implement heal logic
        // IF medicine can revive: process revive effect
        // IF medicine can heal non-fainted status: process status heal
        // IF medicine can heal non-fainted HP: process HP heal
        return null;
    }

    public ProcessResult attack(final AttackAction attackTurn) {
        // TODO: Implement attack logic
        // Process user ailment:
            // Apply damage if applicable
            // Check if ailment prevents move (interrupt multi-turn moves if applicable)
            // Send TurnEvent for user Pokémon
        // Process effects on target:
            // Inflict damage
            // Inflict ailment
            // Inflict stat changes
            // Interrupts enemy multi-turn moves?
            // Send TurnEvent for target Pokémon
        // Process effects on user:
            // Inflict recoil damage
            // Inflict status on self
            // Inflict stat changes on self
            // Send TurnEvent for user Pokémon
        return null;
    }

    public ProcessResult wait(final WaitAction waitAction) {
        // TODO: Implement wait logic
        // Process user ailment:
            // Apply damage if applicable
            // Check if ailment prevents move (interrupt multi-turn moves if applicable)
            // Send TurnEvent for user Pokémon
        // Increment counter for current waiting move
        return null;
    }
}
