package com.mohammed.rickandmorty.fueature.rick_and_morty.data.mappers

import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.remote.rick_and_morty_api.dto.Character

fun Character.toEntity(page: Int): com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character {
    return com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character(
        this.id,
        this.name,
        this.status,
        this.location.name,
        this.origin.name,
        this.image,
        this.species,
        page
    )
}