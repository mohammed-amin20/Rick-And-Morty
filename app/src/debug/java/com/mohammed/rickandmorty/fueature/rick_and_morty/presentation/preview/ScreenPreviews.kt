package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.util.Error
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.detail.CharacterDetailContent
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.detail.CharacterDetailState
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.list.CharacterListContent
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.list.CharacterListState
import com.mohammed.rickandmorty.ui.theme.RickAndMortyTheme

private val rick = Character(
    id = 1,
    name = "Rick Sanchez",
    status = "Alive",
    location = "Citadel of Ricks",
    origin = "Earth (C-137)",
    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
    species = "Human",
    page = 1,
    gender = "Male",
    type = "",
    originUrl = "https://rickandmortyapi.com/api/location/1",
    locationUrl = "https://rickandmortyapi.com/api/location/3",
    created = "2017-11-04T18:48:46.250Z",
    url = "https://rickandmortyapi.com/api/character/1",
    episode = (1..10).toList()
)

private val morty = Character(
    id = 2,
    name = "Morty Smith",
    status = "Alive",
    location = "Citadel of Ricks",
    origin = "Earth (C-137)",
    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
    species = "Human",
    page = 1,
    gender = "Male",
    created = "2017-11-04T18:50:21.651Z",
    url = "https://rickandmortyapi.com/api/character/2",
    episode = (1..10).toList()
)

private val birdperson = Character(
    id = 47,
    name = "Birdperson",
    status = "Dead",
    location = "Replacement Dimension",
    origin = "Bird World",
    image = "https://rickandmortyapi.com/api/character/avatar/47.jpeg",
    species = "Alien",
    page = 1,
    gender = "Male",
    created = "2017-11-05T11:26:11.126Z",
    url = "https://rickandmortyapi.com/api/character/47",
    episode = (1..8).toList()
)

private val mrMeeseeks = Character(
    id = 34,
    name = "Mr. Meeseeks",
    status = "unknown",
    location = "Interdimensional Cable",
    origin = "Mr. Meeseeks Box",
    image = "https://rickandmortyapi.com/api/character/avatar/34.jpeg",
    species = "Humanoid",
    page = 1,
    gender = "Male",
    created = "2017-11-05T09:29:19.664Z",
    url = "https://rickandmortyapi.com/api/character/34",
    episode = (1..5).toList()
)

private val sampleCharacters = listOf(rick, morty, birdperson, mrMeeseeks)

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterListContentPreview() {
    RickAndMortyTheme {
        CharacterListContent(
            state = CharacterListState(characters = sampleCharacters, next = false),
            onSearchChanged = {},
            onRetry = {},
            onLoadMore = {},
            onCharacterClick = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterListPaginationPreview() {
    RickAndMortyTheme {
        CharacterListContent(
            state = CharacterListState(
                characters = sampleCharacters,
                next = true,
                loadingMore = true
            ),
            onSearchChanged = {},
            onRetry = {},
            onLoadMore = {},
            onCharacterClick = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterListLoadingPreview() {
    RickAndMortyTheme {
        CharacterListContent(
            state = CharacterListState(loading = true),
            onSearchChanged = {},
            onRetry = {},
            onLoadMore = {},
            onCharacterClick = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterListEmptyPreview() {
    RickAndMortyTheme {
        CharacterListContent(
            state = CharacterListState(search = "zzzzz"),
            onSearchChanged = {},
            onRetry = {},
            onLoadMore = {},
            onCharacterClick = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterListErrorPreview() {
    RickAndMortyTheme {
        CharacterListContent(
            state = CharacterListState(error = Error.Remote.NoInternet),
            onSearchChanged = {},
            onRetry = {},
            onLoadMore = {},
            onCharacterClick = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailContentPreview() {
    RickAndMortyTheme {
        CharacterDetailContent(
            state = CharacterDetailState(
                id = rick.id,
                name = rick.name,
                image = rick.image,
                character = rick,
                loading = false
            ),
            onRetry = {},
            onBack = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailNoEpisodesPreview() {
    RickAndMortyTheme {
        CharacterDetailContent(
            state = CharacterDetailState(
                id = mrMeeseeks.id,
                name = mrMeeseeks.name,
                image = mrMeeseeks.image,
                character = mrMeeseeks.copy(episode = emptyList()),
                loading = false
            ),
            onRetry = {},
            onBack = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailLoadingPreview() {
    RickAndMortyTheme {
        CharacterDetailContent(
            state = CharacterDetailState(
                id = rick.id,
                name = rick.name,
                image = rick.image,
                loading = true
            ),
            onRetry = {},
            onBack = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailErrorPreview() {
    RickAndMortyTheme {
        CharacterDetailContent(
            state = CharacterDetailState(
                id = rick.id,
                name = rick.name,
                image = rick.image,
                loading = false,
                error = Error.Remote.ServerError
            ),
            onRetry = {},
            onBack = {}
        )
    }
}
