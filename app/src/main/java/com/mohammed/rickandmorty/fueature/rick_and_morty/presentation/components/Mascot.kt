package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mohammed.rickandmorty.ui.theme.PortalBlue
import com.mohammed.rickandmorty.ui.theme.PortalGreen

@Composable
fun Mascot(
    @DrawableRes id: Int,
    modifier: Modifier = Modifier,
    size: Dp = 104.dp,
    glow: Boolean = true,
    bob: Boolean = true,
    contentDescription: String? = null
) {
    val transition = rememberInfiniteTransition(label = "mascot")
    val dy by transition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "mascotBob"
    )

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        if (glow) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { if (bob) translationY = dy }
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                PortalGreen.copy(alpha = 0.30f),
                                PortalBlue.copy(alpha = 0.10f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
        Image(
            painter = painterResource(id),
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { if (bob) translationY = dy }
        )
    }
}
