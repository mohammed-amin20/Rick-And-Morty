package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.repository.RickAndMortyRepository
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: RickAndMortyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterListState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state
                .map { it.search }
                .distinctUntilChanged()
                .debounce(450L)
                .collectLatest { loadFirstPage() }
        }
    }

    fun onAction(action: CharacterListAction) {
        when (action) {
            is CharacterListAction.OnSearchChanged ->
                _state.update { it.copy(search = action.search) }

            CharacterListAction.OnRetry ->
                viewModelScope.launch { loadFirstPage() }

            CharacterListAction.OnNextPageRequested ->
                loadNextPage()
        }
    }

    private suspend fun loadFirstPage() {
        _state.update { it.copy(loading = true, error = null, page = 1, next = true) }
        when (val result = repository.getCharactersByName(page = 1, name = _state.value.search.trim())) {
            is Result.Success -> _state.update {
                it.copy(loading = false, characters = result.data, next = result.data.isNotEmpty())
            }
            is Result.Failure -> _state.update {
                it.copy(loading = false, characters = emptyList(), error = result.error, next = false)
            }
        }
    }

    private fun loadNextPage() {
        val current = _state.value
        if (current.loading || current.loadingMore || !current.next) return

        viewModelScope.launch {
            _state.update { it.copy(loadingMore = true) }
            val nextPage = current.page + 1
            when (val result = repository.getCharactersByName(page = nextPage, name = current.search.trim())) {
                is Result.Success -> _state.update {
                    it.copy(
                        loadingMore = false,
                        page = nextPage,
                        characters = it.characters + result.data,
                        next = result.data.isNotEmpty()
                    )
                }
                is Result.Failure -> _state.update {
                    it.copy(loadingMore = false, next = false)
                }
            }
        }
    }
}
