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
    PokemonDto getPokemonByIdOrName(@PathParam("identifier") final String idName);

    @GET
    @Path("move/{identifier}")
    MoveDto getMoveByIdOrName(@PathParam("identifier") final String idName);

    @GET
    @Path("pokemon-species/{id}")
    SpeciesDto getSpeciesInfoByIdOrName(@PathParam("id") final String idName);

    @GET
    @Path("type/{type}")
    TypeDto getTypeByIdOrName(@PathParam("type") final String typeName);

    @GET
    @Path("generation/{gen}")
    GenerationDto getGenerationByNum(@PathParam("gen") final int genNum);

}
