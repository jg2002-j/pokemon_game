package com.clapped.boundary.rest;

import com.clapped.main.model.ProcessResult;
import com.clapped.main.service.TurnService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/turn")
@ApplicationScoped
public class TurnEndpoint {

    private final TurnService turnService;

    @Inject
    public TurnEndpoint(final TurnService turnService) {
        this.turnService = turnService;
    }

    @POST
    @Path("/attack/{attackerUsername}/{targetUsername}/{moveName}")
    public Response attackPokemon(
            @PathParam("attackerUsername") final String attackerUsername,
            @PathParam("targetUsername") final String targetUsername,
            @PathParam("moveName") final String moveName
    ) {
        return getResponse(turnService.attackAction(attackerUsername, targetUsername, moveName));
    }

    @PATCH
    @Path("/heal/{username}/{medicineName}")
    public Response healPokemon(
            @PathParam("username") final String username,
            @PathParam("medicineName") final String medicineName
    ) {
        return getResponse(turnService.healAction(username, medicineName));
    }

    @POST
    @Path("/switch/{username}/{newIndex}")
    public Response switchPokemon(
            @PathParam("username") final String username,
            @PathParam("newIndex") final int newIndex
    ) {
        return getResponse(turnService.switchAction(username, newIndex));
    }

    private Response getResponse(final List<ProcessResult> results) {
        if (!results.isEmpty() && results.stream().allMatch(ProcessResult::isSuccess)) {
            return Response.ok(results.stream().map(ProcessResult::getMessage).toList()).build();
        } else {
            List<String> erroredResults = results.stream().filter(res -> !res.isSuccess()).map(ProcessResult::getMessage).toList();
            return Response.status(Response.Status.BAD_REQUEST).entity(erroredResults).build();
        }
    }

}
