package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.detail

sealed interface CharacterDetailAction {
    data object OnRetry : CharacterDetailAction
}
