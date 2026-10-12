package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mohammed.rickandmorty.R
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.util.Error

@Composable
fun EmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    StateMessage(
        modifier = modifier,
        badge = {
            Mascot(
                id = R.drawable.ic_chibi_morty_wow,
                contentDescription = null
            )
        },
        title = title,
        message = message
    )
}

@Composable
fun ErrorState(
    error: Error,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Mascot(
            id = R.drawable.ic_chibi_rick,
            contentDescription = null
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Aw jeez… something broke.",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = error.toDisplayMessage(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Try again")
        }
    }
}

@Composable
private fun StateMessage(
    badge: @Composable () -> Unit,
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        badge()
        Spacer(Modifier.height(20.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

fun Error.toDisplayMessage(): String = when (this) {
    Error.Remote.NoInternet -> "No portal signal. Check your internet and try again, Morty."
    Error.Remote.NotFound -> "Couldn't find that in any dimension."
    Error.Remote.ServerError -> "The Citadel's servers are having a rough day. Try again shortly."
    Error.Remote.Timeout -> "The portal took too long to open. Give it another shot."
    Error.Remote.TooManyRequests -> "Whoa, slow down, Morty. Too many requests."
    Error.Remote.JsonParseError -> "We picked up a weird signal from the multiverse."
    Error.Remote.BadRequest -> "That request came out a little squanchy."
    Error.Remote.Unauthorized -> "You're not cleared for this dimension."
    is Error.Remote.Unknown -> message
    is Error.Local.Unknown -> message
    Error.Local.DiskFull -> "Your device is out of space, Morty."
}
