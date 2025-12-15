package com.clapped.main.service;

import com.clapped.boundary.rest.dto.PlayerDto;
import com.clapped.main.messaging.events.EventType;
import com.clapped.main.messaging.events.PlayerEvent;
import com.clapped.main.messaging.events.PlayerEvtType;
import com.clapped.main.messaging.producer.PlayerEventProducer;
import com.clapped.main.model.Player;
import com.clapped.main.model.ProcessResult;
import com.clapped.pokemon.model.Pokemon;
import com.clapped.pokemon.service.PokemonService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@ApplicationScoped
public class PlayerService {

    public static final String ERROR_PREFIX = "ERROR: ";
    private final GameState gameState;
    private final PokemonService pokeService;
    private final PlayerEventProducer playerEventProducer;

    @Inject
    public PlayerService(
            final GameState gameState,
            final PokemonService pokeService,
            final PlayerEventProducer playerEventProducer
    ) {
        this.gameState = gameState;
        this.pokeService = pokeService;
        this.playerEventProducer = playerEventProducer;
    }

    public ProcessResult join(final PlayerDto dto) {
        try {

            final String username = dto.username();
            final int teamNum = dto.teamNum();
            final List<String> pokemonIds = Stream.of(
                    dto.slot1(), dto.slot2(), dto.slot3(), dto.slot4(), dto.slot5(), dto.slot6()
            ).filter(Objects::nonNull).toList();

            ProcessResult invalidPlayerInfoOutcome = validatePlayerInfo(username, teamNum);
            if (invalidPlayerInfoOutcome != null) return invalidPlayerInfoOutcome;

            final LinkedList<Pokemon> pokemonTeam = new LinkedList<>();
            final int pokemonTeamSize = Math.min(pokemonIds.size(), 6);
            for (int i = 0; i < pokemonTeamSize; i++) {
                final String id = pokemonIds.get(i);
                final Pokemon pokemon = pokeService.getPokemonByIdOrName(id);
                if (pokemon == null) {
                    return ProcessResult.error("Failed to find Pokemon with id: " + id + "; ignoring JOIN");
                }
                pokemonTeam.add(pokemon);
            }

            final Player player = new Player(
                    username,
                    teamNum,
                    pokemonTeam
            );

            gameState.addPlayer(username, player);
            final String msg = String.format(
                    "JOIN: %s joined Team %d with %s!",
                    username, teamNum, pokemonTeam.stream().map(Pokemon::toPrettyString).toList()
            );
            final ProcessResult result = ProcessResult.success(msg);
            playerEventProducer.sendPlayerEvent(new PlayerEvent(
                    System.currentTimeMillis(), EventType.PLAYER_EVENT, PlayerEvtType.JOIN, player, result
            ));
            return result;
        } catch (Exception ex) {
            log.error(ERROR_PREFIX, ex);
            return ProcessResult.error("Failed to handle team event: " + ex.getMessage());
        }
    }

    private ProcessResult validatePlayerInfo(String username, int teamNum) {
        if (teamNum != 1 && teamNum != 2) {
            return ProcessResult.error("Invalid team number (" + teamNum + "); ignoring JOIN");
        }
        if (gameState.getPlayersForTeam(teamNum).size() >= 5) {
            return ProcessResult.error("Team " + teamNum + " is already full; ignoring JOIN");
        }
        if (gameState.hasPlayer(username)) {
            return ProcessResult.error(username + " already in the game; ignoring JOIN");
        }
        return null;
    }

    public ProcessResult leave(final String username) {
        try {
            Player removed = gameState.removePlayer(username);
            if (removed == null) return ProcessResult.error("Failed to find player: " + username + "; ignoring LEAVE");
            String msg = "LEAVE: Player " + username + " left Team " + removed.getTeamNum();
            final ProcessResult result = ProcessResult.success(msg);
            playerEventProducer.sendPlayerEvent(
                    new PlayerEvent(System.currentTimeMillis(), EventType.PLAYER_EVENT, PlayerEvtType.LEAVE, removed, result)
            );
            return result;
        } catch (Exception ex) {
            log.error(ERROR_PREFIX, ex);
            return ProcessResult.error("Failed to handle team event: " + ex.getMessage());
        }
    }
}
