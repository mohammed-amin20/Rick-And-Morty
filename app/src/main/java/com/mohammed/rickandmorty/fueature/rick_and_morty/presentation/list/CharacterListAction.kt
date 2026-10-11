package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.list

sealed interface CharacterListAction {
    data class OnSearchChanged(val search: String) : CharacterListAction
    data object OnNextPageRequested : CharacterListAction
    data object OnRetry : CharacterListAction
}
