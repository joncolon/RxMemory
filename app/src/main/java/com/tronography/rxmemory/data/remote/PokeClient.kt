package com.tronography.rxmemory.data.remote

import com.tronography.rxmemory.data.model.common.NamedApiResourceList
import com.tronography.rxmemory.data.model.pokemon.PokemonResponse
import com.tronography.rxmemory.data.model.species.SpeciesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokeClient {

    @GET(POKEMON_PATH_ALL_V2)
    fun getPokemonList(
            @Query("limit") limit: Int?,
            @Query("offset") offset: Int?
    ): Observable<NamedApiResourceList>

    @GET(POKEMON_PATH_ID_V2)
    fun getPokemon(@Path("id") id: Int): Observable<PokemonResponse>

    @GET(POKEMON_PATH_NAME_V2)
    fun getPokemon(@Path("name") name: String): Observable<PokemonResponse>

    @GET(POKEMON_SPECIES_PATH_ID_V2)
    fun getSpecies(@Path("id") id: Int): Observable<SpeciesResponse>

    @GET(POKEMON_SPECIES_PATH_NAME_V2)
    fun getSpecies(@Path("name") name: String): Observable<SpeciesResponse>

    companion object {

        const val POKEMON_PATH_ALL_V2 = "/api/v2/pokemon/"

        const val POKEMON_PATH_NAME_V2 = "/api/v2/pokemon/{name}"

        const val POKEMON_PATH_ID_V2 = "/api/v2/pokemon/{id}"

        const val POKEMON_SPECIES_PATH_NAME_V2 = "/api/v2/pokemon-species/{name}"

        const val POKEMON_SPECIES_PATH_ID_V2 = "/api/v2/pokemon-species/{id}"

        const val DOMAIN = "https://pokeapi.co"

    }

}

