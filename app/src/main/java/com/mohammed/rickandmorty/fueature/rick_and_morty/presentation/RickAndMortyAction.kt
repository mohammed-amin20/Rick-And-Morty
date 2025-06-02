package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation

sealed interface RickAndMortyAction {
    data class OnSearchChanged(val search: String) : RickAndMortyAction
    data object OnNextPageRequested : RickAndMortyAction
}