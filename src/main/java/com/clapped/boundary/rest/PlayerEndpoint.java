package com.clapped.boundary.rest;

import com.clapped.boundary.rest.dto.PlayerDto;
import com.clapped.main.model.ProcessResult;
import com.clapped.main.service.PlayerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Slf4j
@Path("/player")
@ApplicationScoped
public class PlayerEndpoint {

    private final PlayerService playerService;

    @Inject
    public PlayerEndpoint(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @POST
    @Path("/join")
    public Response join(@RequestBody final PlayerDto playerDto) {
        final ProcessResult result = playerService.join(playerDto);
        return result.isSuccess()
                ? Response.ok(result.getMessage()).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.getMessage()).build();
    }

    @DELETE
    @Path("/leave/{userId}")
    public Response leave(
            @PathParam("userId") final String userId
    ) {
        final ProcessResult result = playerService.leave(userId);
        return result.isSuccess()
                ? Response.ok(result.getMessage()).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.getMessage()).build();
    }

}
