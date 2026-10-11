package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.EpisodeChip
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.ErrorState
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.InfoChip
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.InfoRow
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.LoadingPortal
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.SectionCard
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.StatusBadge
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.StatusDot
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components.statusColor
import com.mohammed.rickandmorty.ui.theme.PortalGreen
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CharacterDetailScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CharacterDetailContent(
        state = state,
        onRetry = { viewModel.onAction(CharacterDetailAction.OnRetry) },
        onBack = onBack,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@Composable
fun CharacterDetailContent(
    state: CharacterDetailState,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Hero(
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    id = state.id,
                    name = state.name,
                    image = state.image,
                    status = state.character?.status.orEmpty()
                )
                Spacer(Modifier.height(20.dp))
                DetailBody(
                    state = state,
                    onRetry = onRetry
                )
                Spacer(Modifier.height(32.dp))
            }

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(innerPadding)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xCC1B1E24))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun Hero(
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?,
    id: Int,
    name: String,
    image: String,
    status: String
) {
    val imageModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            Modifier.sharedElement(
                sharedContentState = rememberSharedContentState(key = "character-image-$id"),
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    } else {
        Modifier
    }

    val nameModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            Modifier.sharedElement(
                sharedContentState = rememberSharedContentState(key = "character-name-$id"),
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        AsyncImage(
            model = image,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .then(imageModifier)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color(0x66000000),
                        0.4f to Color.Transparent,
                        1f to Color(0xE6111316)
                    )
                )
        )
        if (status.isNotBlank()) {
            StatusBadge(
                status = status,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.displaySmall,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(20.dp)
                .then(nameModifier)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailBody(
    state: CharacterDetailState,
    onRetry: () -> Unit
) {
    val character = state.character

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            state.loading && character == null -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                LoadingPortal()
            }

            state.error != null && character == null -> ErrorState(
                error = state.error,
                onRetry = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )

            character != null -> {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoChip(
                        label = character.status.ifBlank { "Unknown" },
                        containerColor = statusColor(character.status).copy(alpha = 0.18f),
                        contentColor = statusColor(character.status),
                        leading = { StatusDot(status = character.status) }
                    )
                    InfoChip(label = character.species.ifBlank { "Unknown" })
                    InfoChip(label = character.gender.ifBlank { "Unknown" })
                }

                SectionCard(title = "Profile") {
                    InfoRow(
                        icon = Icons.Default.Person,
                        label = "Gender",
                        value = character.gender
                    )
                    InfoRow(
                        icon = Icons.Default.Info,
                        label = "Type",
                        value = character.type.ifBlank { "Unknown" }
                    )
                    InfoRow(
                        icon = Icons.Default.Info,
                        label = "Created",
                        value = formatCreated(character.created)
                    )
                }

                SectionCard(title = "Origin") {
                    InfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "Origin",
                        value = character.origin
                    )
                }

                SectionCard(title = "Last known location") {
                    InfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "Location",
                        value = character.location,
                        iconTint = PortalGreen
                    )
                }

                SectionCard(title = "Appearances (${character.episode.size})") {
                    if (character.episode.isEmpty()) {
                        Text(
                            text = "No episode data",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            character.episode.forEach { episode ->
                                EpisodeChip(number = episode)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatCreated(created: String): String {
    if (created.isBlank()) return "Unknown"
    return try {
        Instant.parse(created)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
    } catch (_: Exception) {
        created
    }
}
