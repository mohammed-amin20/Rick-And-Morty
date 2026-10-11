package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object CharactersRoute

@Serializable
data class CharacterDetailRoute(
    val id: Int,
    val name: String,
    val image: String
)
