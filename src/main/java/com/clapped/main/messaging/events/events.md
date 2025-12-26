Info that i need to send from engine to gui:

### SettingsEvent

Sent once at startup and each time any setting is changed

- `int`: Gen change
- `int`: Level change
- `List<String>`: 1 or more log messages: "Generation was updated to V.", "Level was updated to 20."

### LobbyEvent

Sent once per JOIN/LEAVE

- `Player`: Player who joined/left
- `Enum`: JOIN/LEAVE flag
- `String`: 1 Log message, e.g. "Player1 joined the game with [SQUIRTLE]"

### Game

#### TurnStartEvent

Sent for every new turn

- `int`: Turn change
- `Map<Username, List<Option>>`: Options players have for this turn (attack, wait, switch, none)
- `String`: 1 Log message, e.g. "Turn 2 started."

#### TurnQueueEvent

Sent for every new queued action

- `List<Username>`: Players who have queued an action for this turn
- `String`: 1 Log message, last queued action: "Player1's SQUIRTLE will use BUBBLE on Player2's CHARMANDER."
- `List<Username>`: Players who need to queue an action

#### ProcessedTurnEvent

Sent for every processed action

- `List<Player>`: 1 or more changed players (their pokemons' pp, hp, stats, status, faint, etc.)
- `List<String>`: 1 or more log messages, e.g. "Player1's SQUIRTLE used BUBBLE on Player2's CHARMANDER.", "Player2's
  CHARMANDER fainted..."
