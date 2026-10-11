package com.mohammed.rickandmorty.fueature.rick_and_morty.data.mappers

import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character as CharacterEntity
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.remote.rick_and_morty_api.dto.Character

fun Character.toEntity(page: Int): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        location = location.name,
        origin = origin.name,
        image = image,
        species = species,
        page = page,
        gender = gender,
        type = type,
        originUrl = origin.url,
        locationUrl = location.url,
        created = created,
        url = url,
        episode = episode.mapNotNull { it.substringAfterLast('/').toIntOrNull() }
    )
}
