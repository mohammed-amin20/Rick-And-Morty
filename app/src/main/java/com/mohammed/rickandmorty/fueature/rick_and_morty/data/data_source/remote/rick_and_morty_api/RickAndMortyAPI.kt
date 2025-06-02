package com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.remote.rick_and_morty_api

import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.remote.rick_and_morty_api.dto.CharactersDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET("api/character")
    suspend fun getCharactersByName(
        @Query("name") name : String,
        @Query("page") page: Int
    ): CharactersDto
}