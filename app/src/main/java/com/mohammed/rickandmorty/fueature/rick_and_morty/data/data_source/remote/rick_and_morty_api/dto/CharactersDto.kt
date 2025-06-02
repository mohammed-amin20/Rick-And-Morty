package com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.remote.rick_and_morty_api.dto

data class CharactersDto(
    val info: Info?,
    val results: List<Character> = emptyList(),
    val error: String?
)