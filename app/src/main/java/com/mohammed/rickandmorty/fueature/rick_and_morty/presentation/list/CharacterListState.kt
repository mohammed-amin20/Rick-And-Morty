package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.list

import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.util.Error

data class CharacterListState(
    val characters: List<Character> = emptyList(),
    val search: String = "",
    val loading: Boolean = false,
    val loadingMore: Boolean = false,
    val page: Int = 1,
    val next: Boolean = true,
    val error: Error? = null
)
