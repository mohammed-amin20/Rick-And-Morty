package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation

import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character

data class RickAndMortyState(
    val characters : List<Character> = emptyList(),
    val search : String = "",
    val loading : Boolean = false,
    val page: Int = 1,
    val next: Boolean = true
)
