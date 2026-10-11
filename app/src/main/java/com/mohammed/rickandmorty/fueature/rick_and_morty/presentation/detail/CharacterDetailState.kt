package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.detail

import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.util.Error

data class CharacterDetailState(
    val id: Int,
    val name: String,
    val image: String,
    val character: Character? = null,
    val loading: Boolean = true,
    val error: Error? = null
)
