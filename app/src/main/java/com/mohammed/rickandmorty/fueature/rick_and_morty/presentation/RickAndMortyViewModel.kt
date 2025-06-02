package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.repository.RickAndMortyRepository
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.util.Error
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RickAndMortyViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RickAndMortyState())
    val state = _state.asStateFlow()

    private val _uiAction = MutableSharedFlow<UiAction>()
    val uiAction = _uiAction.asSharedFlow()

    init {
        viewModelScope.launch {
            _state
                .map { it.search }
                .distinctUntilChanged()
                .debounce { 500L }
                .collect {
                    if(_state.value.search.isNotBlank()) {
                        getCharactersByName()
                    }
                }
        }
    }

    private suspend fun getCharactersByName() {
        _state.update {
            it.copy(
                loading = true
            )
        }
        val result = repository.getCharactersByName(
            page = _state.value.page,
            name = _state.value.search.trim()
        )
        when(result) {
            is Result.Failure -> {
                when(result.error) {
                    Error.Local.DiskFull -> {
//                        _uiAction.emit(UiAction.ShowToast("Disk full"))
                    }
                    is Error.Local.Unknown -> {
                        _uiAction.emit(UiAction.ShowToast(result.error.message))
                    }
                    Error.Remote.BadRequest -> {
//                        _uiAction.emit(UiAction.ShowToast("Bad request"))
                    }
                    Error.Remote.JsonParseError -> {
//                        _uiAction.emit(UiAction.ShowToast("Parsing error"))
                    }
                    Error.Remote.NoInternet -> {
                        _uiAction.emit(UiAction.ShowToast("No internet connection"))
                    }
                    Error.Remote.NotFound -> {
//                        _uiAction.emit(UiAction.ShowToast("Not found"))
                    }
                    Error.Remote.ServerError -> {
//                        _uiAction.emit(UiAction.ShowToast("Server error"))
                    }
                    Error.Remote.Timeout -> {
//                        _uiAction.emit(UiAction.ShowToast("Timeout"))
                    }
                    Error.Remote.TooManyRequests -> {
//                        _uiAction.emit(UiAction.ShowToast("Too many requests"))
                    }
                    Error.Remote.Unauthorized -> {
//                        _uiAction.emit(UiAction.ShowToast("Unauthorized"))
                    }
                    is Error.Remote.Unknown -> {
                        _uiAction.emit(UiAction.ShowToast(result.error.message))
                    }
                }
                _state.update {
                    it.copy(
                        next = false
                    )
                }
            }
            is Result.Success -> {
                _state.update {
                    it.copy(
                        characters = _state.value.characters + result.data
                    )
                }
            }
        }
        _state.update {
            it.copy(
                loading = false
            )
        }
    }

    fun onAction(action: RickAndMortyAction) {
        when (action) {
            is RickAndMortyAction.OnSearchChanged -> {
                _state.update {
                    it.copy(
                        search = action.search,
                        page = 1,
                        next = true,
                        characters = emptyList()
                    )
                }
            }

            RickAndMortyAction.OnNextPageRequested -> {
                _state.update {
                    it.copy(
                        page = it.page + 1
                    )
                }
                viewModelScope.launch {
                    if(_state.value.next) {
                        getCharactersByName()
                    }
                }
            }
        }
    }

    sealed interface UiAction {
        data class ShowToast(val message: String) : UiAction
    }
}