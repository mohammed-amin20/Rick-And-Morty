package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.AnimatedEntrance
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.CharacterCard
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.CharacterSkeletonCard
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.EmptyState
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.ErrorState
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.LoadingPortal
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.SearchField
import com.mohammed.rickandmorty.ui.theme.PortalGreen

@Composable
fun CharacterListScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onCharacterClick: (Character) -> Unit,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CharacterListContent(
        state = state,
        onSearchChanged = { viewModel.onAction(CharacterListAction.OnSearchChanged(it)) },
        onRetry = { viewModel.onAction(CharacterListAction.OnRetry) },
        onLoadMore = { viewModel.onAction(CharacterListAction.OnNextPageRequested) },
        onCharacterClick = onCharacterClick,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@Composable
fun CharacterListContent(
    state: CharacterListState,
    onSearchChanged: (String) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onCharacterClick: (Character) -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            total > 0 && lastVisible >= total - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Header()
            Spacer(Modifier.height(16.dp))
            SearchField(
                value = state.search,
                onValueChange = onSearchChanged
            )
            Spacer(Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.loading && state.characters.isEmpty() -> SkeletonColumn()

                    state.error != null && state.characters.isEmpty() -> ErrorState(
                        error = state.error,
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize()
                    )

                    state.characters.isEmpty() -> EmptyState(
                        title = "No characters found",
                        message = "The multiverse came up empty. Try a different name.",
                        modifier = Modifier.fillMaxSize()
                    )

                    else -> LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(
                            items = state.characters,
                            key = { _, character -> character.id }
                        ) { index, character ->
                            AnimatedEntrance(index = index) {
                                SharedCharacterCard(
                                    character = character,
                                    onClick = { onCharacterClick(character) },
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                            }
                        }

                        if (state.next) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LoadingPortal(size = 40.dp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SharedCharacterCard(
    character: Character,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?
) {
    val imageModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            Modifier.sharedElement(
                sharedContentState = rememberSharedContentState(key = "character-image-${character.id}"),
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    } else {
        Modifier
    }

    val nameModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            Modifier.sharedElement(
                sharedContentState = rememberSharedContentState(key = "character-name-${character.id}"),
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    } else {
        Modifier
    }

    CharacterCard(
        character = character,
        onClick = onClick,
        imageModifier = imageModifier,
        nameModifier = nameModifier
    )
}

@Composable
private fun Header() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Rick & Morty",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Explore the multiverse",
            style = MaterialTheme.typography.bodyLarge,
            color = PortalGreen
        )
    }
}

@Composable
private fun SkeletonColumn() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(3) {
            CharacterSkeletonCard()
        }
    }
}
