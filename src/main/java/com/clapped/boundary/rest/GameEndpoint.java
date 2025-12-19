package com.clapped.boundary.rest;

import com.clapped.boundary.rest.dto.GameSettingsDto;
import com.clapped.boundary.rest.dto.PlayerDto;
import com.clapped.main.model.ProcessResult;
import com.clapped.main.service.GameService;
import com.clapped.main.service.GameState;
import com.clapped.main.service.PlayerService;
import com.clapped.main.service.TurnService;
import com.clapped.main.util.RandomProvider;
import com.clapped.main.util.SecureRandomProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Path("")
@ApplicationScoped
public class GameEndpoint {

    private final GameService gameService;
    private final GameState gameState;
    private final PlayerService playerService;
    private final TurnService turnService;
    private final RandomProvider randomProvider;

    @Inject
    public GameEndpoint(
            final GameService gameService,
            final GameState gameState,
            final PlayerService playerService,
            final TurnService turnService,
            final SecureRandomProvider randomProvider
    ) {
        this.gameService = gameService;
        this.gameState = gameState;
        this.playerService = playerService;
        this.turnService = turnService;
        this.randomProvider = randomProvider;
    }

    @POST
    @Path("/game/finaliseTeams")
    public Response finaliseTeams() {
        final ProcessResult result = gameService.finaliseTeams();
        return result.isSuccess()
                ? Response.ok(result.getMessage()).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.getMessage()).build();
    }

    @POST
    @Path("/settings/update")
    public Response updateSettings(@RequestBody GameSettingsDto dto) {
        final List<ProcessResult> results = gameService.updateSettings(dto);
        return results.stream().allMatch(ProcessResult::isSuccess)
            ? Response.ok(results.stream().map(ProcessResult::getMessage).toList()).build()
            : Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(results.stream().map(ProcessResult::getMessage).toList()).build();
    }

    @GET
    @Path("/util/getTurnNum")
    public Response getTurn() {
        final int turn = turnService.getTurnNum();
        return Response.ok(turn).build();
    }

    @POST
    @Path("/util/randomTeams")
    public Response initialiseTeams() {
        int teamNum = 1;
        List<ProcessResult> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final PlayerDto playerDto = new PlayerDto(
                    "player" + i,
                    "https://play.pokemonshowdown.com/sprites/trainers/red.png",
                    teamNum,
                    List.of(
                        getRandomPokemonId(),
                        getRandomPokemonId(),
                        getRandomPokemonId(),
                        getRandomPokemonId(),
                        getRandomPokemonId(),
                        getRandomPokemonId()
                    )
            );
            final ProcessResult result = playerService.join(playerDto);
            results.add(result);
            teamNum = teamNum == 1 ? 2 : 1;
        }
        return results.stream().allMatch(ProcessResult::isSuccess)
                ? Response.ok(results.stream().map(ProcessResult::getMessage).toList()).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(results.stream().map(ProcessResult::getMessage).toList()).build();
    }

    private String getRandomPokemonId() {
        return String.valueOf(randomProvider.getRandInt(1, gameState.getPokemonGen().getNumPokemonInGen()));
    }

}
