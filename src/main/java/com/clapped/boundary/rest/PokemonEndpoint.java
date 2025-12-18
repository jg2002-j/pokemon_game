package com.clapped.boundary.rest;

import com.clapped.boundary.rest.dto.SimplePkmnDto;
import com.clapped.pokemon.model.Pokemon;
import com.clapped.pokemon.service.PokemonService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

@Path("/pokemon")
@ApplicationScoped
public class PokemonEndpoint {

    private final PokemonService service;

    @Inject
    public PokemonEndpoint(final PokemonService service) {
        this.service = service;
    }

    @GET
    @Path("/{id_or_name}")
    public Pokemon getPokemonByIdOrName(@DefaultValue("pikachu") @PathParam("id_or_name") final String identifier) {
        return service.getPokemonByIdOrName(identifier);
    }

    @GET
    @Path("/validForGen/{gen}")
    public List<SimplePkmnDto> getAllValidPokemonForGen(@DefaultValue("5") @PathParam("gen") final int genNum) {
        return service.getAllValidPkmnForGen(genNum);
    }

}
