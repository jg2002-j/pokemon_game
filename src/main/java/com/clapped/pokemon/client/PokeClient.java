package com.clapped.pokemon.client;

import com.clapped.pokemon.model.dto.generation.GenerationDto;
import com.clapped.pokemon.model.dto.move.MoveDto;
import com.clapped.pokemon.model.dto.pokemon.PokemonDto;
import com.clapped.pokemon.model.dto.species.SpeciesDto;
import com.clapped.pokemon.model.dto.type.TypeDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "poke-api")
public interface PokeClient {

    @GET
    @Path("pokemon/")
    String getAllPokemon(@QueryParam("limit") final int limit, @QueryParam("offset") final int offset);

    @GET
    @Path("pokemon/{identifier}")
    PokemonDto getPokemon(@PathParam("identifier") final String identifier);

    @GET
    @Path("move/{identifier}")
    MoveDto getMove(@PathParam("identifier") final String identifier);

    @GET
    @Path("pokemon-species/{identifier}")
    SpeciesDto getSpecies(@PathParam("identifier") final String identifier);

    @GET
    @Path("type/{type}")
    TypeDto getType(@PathParam("type") final String typeName);

    @GET
    @Path("generation/{gen}")
    GenerationDto getGeneration(@PathParam("gen") final int genNum);

}
