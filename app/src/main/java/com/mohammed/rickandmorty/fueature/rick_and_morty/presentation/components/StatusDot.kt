package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohammed.rickandmorty.ui.theme.StatusAlive
import com.mohammed.rickandmorty.ui.theme.StatusDead
import com.mohammed.rickandmorty.ui.theme.StatusUnknown

fun statusColor(status: String): Color = when (status.lowercase()) {
    "alive" -> StatusAlive
    "dead" -> StatusDead
    else -> StatusUnknown
}

@Composable
fun StatusDot(
    status: String,
    modifier: Modifier = Modifier,
    size: Dp = 10.dp,
    pulse: Boolean = false
) {
    val color = statusColor(status)
    val alpha = if (pulse) {
        val transition = rememberInfiniteTransition(label = "statusPulse")
        val value by transition.animateFloat(
            initialValue = 0.35f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
            label = "statusAlpha"
        )
        value
    } else {
        1f
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color.copy(alpha = alpha))
    )
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val color = statusColor(status)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xCC1B1E24))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = status.ifBlank { "Unknown" },
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
