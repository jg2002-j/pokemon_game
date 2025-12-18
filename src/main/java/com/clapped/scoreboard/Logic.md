## Pre-Game
1. Open frontend
2. Connects WebSocket
3. User completes game settings (generation, level, showdownIcons)
   - Goes to Main service via REST POST calls
   - Main sends to GUI service via Kafka
   - GUI service sends to Frontend via WebSocket
4. User enters in players with their pokemon choices
   - Goes to Main service via REST
   - Main sends to GUI service via Kafka
   - GUI service sends each **PLAYER** to Frontend via WebSocket
5. User starts game
   - Goes to Main via REST
   - Main service starts new Turn, calculates PlayerTurnOptions for all players
   - Sends PlayerTurnOptions to GUIservice via Kafka
   - WebSocket updates frontend to have both **TURN NUM** and **PLAYER OPTIONS** for all players

## Gameplay
1. Player chooses turn action
2. Completes it in a form and sends to Main service via REST
3. Once all turns collected, Main service processes turns
4. Each completed turn is sent to GUIservice via Kafka - contains an altered player
5. New player info is sent to frontend via WebSocket

## Scoreboard
1. A 5v5 view of the whole game
2. GUI Service will eventually have a kafka topology to collect metrics on players/turns