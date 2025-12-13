# Game Logic

# 1. Game Initialisation

1. GUI started
2. Send [`GameEvent`](main/messaging/events/GameEvent.java) with `LEVEL_CHANGE` containing default level
3. Send [`GameEvent`](main/messaging/events/GameEvent.java) with `GENERATION_CHANGE` containing default generation
4. Each user submits a username and a team of up to 6 Pokémon
    - IF username is valid AND all Pokémon are valid: save to game state and send [
      `PlayerEvent`](main/messaging/events/PlayerEvent.java)
5. Any user finishes team creation via endpoint.

---

# 2. Turn Flow

## 2A. Start a new turn

1. Increment turn number by 1.
2. Send new [`GameEvent`](main/messaging/events/GameEvent.java) with `TURN_CHANGE` and `turnNum`.
3. FOR EACH PLAYER determine what type of turns they can take:
    1. IF active Pokémon is fainted, check if player has any non-fainted Pokémon:
        1. IF all player's Pokémon are fainted, options are `NONE`
        2. ELSE: player options are only `SWITCH`
    2. IF player's active Pokémon is in a multi-turn move (dig, solarbeam, hyper beam, etc.):
        - Player options are only `WAIT`
    3. ELSE (can have turn): each player can choose one of:
        - SWITCH
        - HEAL
        - ATTACK
4. Send [`TurnInfoEvent`](main/messaging/events/TurnInfoEvent.java) with details of what options each player has.
5. For all players with only `WAIT` automatically add those to `queuedActions` (no user input required).
6. Remaining players require an input for their turn.

--- 

## 2B. Players submit actions for turn

1. Each player can submit their turn
2. IF turn is a valid choice according to spec below, add to `queuedActions`

### Initial player action validation

These validations occur at an endpoint level, and the app won't be able to proceed without passing.

#### A. [`SwitchAction`](main/model/SwitchAction.java)

1. Choose replacement Pokémon index
2. **Validate:**
    - Pokémon at index is not null AND not fainted

#### B. [`HealAction`](main/model/HealAction.java)

1. Choose player to pick Pokémon from
2. Choose Pokémon on team to heal
3. Choose medicine to use
4. **Validate:**
    - Player is not null AND Pokémon is not null
    - IF Pokémon is fainted: selected medicine can revive
    - ELSE IF: (Pokémon needs status heal AND medicine can heal status) OR (Pokémon needs HP heal AND medicine
      can heal
      HP)

#### C. [`AttackAction`](main/model/AttackAction.java)

1. Choose move to use
2. Choose target player (actual target is their `activePokemon`)
3. **Validate:**
    - Move is a valid option in turn player's active Pokémon moveset: exists, non-zero PP
    - Target player is not null AND target Pokémon is not null AND target Pokémon is not fainted

#### D. [`WaitAction`](main/model/WaitAction.java)

1. Automatically assigned — no player input required

---

## 2C. End existing turn

1. Once no more user inputs are required, end of turn is automatically triggered:
2. Each action is sorted:
    1. [`SwitchAction`](main/model/SwitchAction.java) — always first
    2. [`HealAction`](main/model/HealAction.java) — second priority
    3. [`AttackAction`](main/model/AttackAction.java) and [`WaitAction`](main/model/WaitAction.java) — sorted by:
        1. Move priority (higher first)
        2. Current Pokémon speed (higher first, if priority equal)
2. For each turn in order, they are processed according to specs below.
3. New turn is started.

### Turn Processing

There is additional player action validation here, but these count as a successful turn even if they do not trigger
additional effects.

#### A. [`SwitchAction`](main/model/SwitchAction.java)

1. Swap player's active Pokémon to `newPokemonIndex`
2. Send [`TurnEvent`](main/messaging/events/TurnEvent.java)

#### B. [`HealAction`](main/model/HealAction.java)

1. IF medicine can revive: process revive effect
2. IF medicine can heal non-fainted status: process status heal
3. IF medicine can heal non-fainted HP: process HP heal
4. Send [`TurnEvent`](main/messaging/events/TurnEvent.java)

#### C. [`AttackAction`](main/model/AttackAction.java)

1. **Process user ailment:**
    - Apply damage if applicable
    - Check if ailment prevents move (interrupt multi-turn moves if applicable)
    - Send [`TurnEvent`](main/messaging/events/TurnEvent.java) for user Pokémon
2. **Process effects on target:**
    - Inflict damage
    - Inflict ailment
    - Inflict stat changes
    - Interrupts enemy multi-turn moves?
    - Send [`TurnEvent`](main/messaging/events/TurnEvent.java) for target Pokémon
3. **Process effects on user:**
    - Inflict recoil damage
    - Inflict status on self
    - Inflict stat changes on self
    - Send [`TurnEvent`](main/messaging/events/TurnEvent.java) for user Pokémon

#### D. [`WaitAction`](main/model/WaitAction.java)

1. **Process user ailment:**
    - Apply damage if applicable
    - Check if ailment prevents move (interrupt multi-turn moves if applicable)
    - Send [`TurnEvent`](main/messaging/events/TurnEvent.java) for user Pokémon
2. Increment counter for current waiting move
3. Send [`TurnEvent`](main/messaging/events/TurnEvent.java)