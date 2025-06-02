package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.CharacterItem

@Composable
fun RickAndMortyScreen(
    viewModel: RickAndMortyViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val lazyState = rememberLazyListState()

    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.uiAction.collect { uiAction ->
            when(uiAction) {
                is RickAndMortyViewModel.UiAction.ShowToast -> {
                    Toast.makeText(context, uiAction.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if(!lazyState.canScrollForward && state.value.characters.isNotEmpty()) {
        viewModel.onAction(RickAndMortyAction.OnNextPageRequested)
    }

    Scaffold (
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = Color(0xFF272B33)
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier.fillMaxSize()
            ){
                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = lazyState
                ){
                    item {
                        Spacer(Modifier.height(16.dp))
                    }

                    items(state.value.characters) { item ->
                        CharacterItem(item)
                    }

                    if(state.value.next && state.value.characters.isNotEmpty()) {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                TextField(
                    value = state.value.search,
                    onValueChange = {
                        viewModel.onAction(RickAndMortyAction.OnSearchChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if(state.value.loading && state.value.characters.isEmpty())
                CircularProgressIndicator()
        }
    }
}