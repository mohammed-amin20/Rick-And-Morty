package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.repository.RickAndMortyRepository
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.util.Result
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.navigation.CharacterDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<CharacterDetailRoute>()

    private val _state = MutableStateFlow(
        CharacterDetailState(id = route.id, name = route.name, image = route.image)
    )
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun onAction(action: CharacterDetailAction) {
        when (action) {
            CharacterDetailAction.OnRetry -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            when (val result = repository.getCharacter(_state.value.id)) {
                is Result.Success -> _state.update {
                    it.copy(loading = false, character = result.data, error = null)
                }
                is Result.Failure -> _state.update {
                    it.copy(loading = false, error = result.error)
                }
            }
        }
    }
}
