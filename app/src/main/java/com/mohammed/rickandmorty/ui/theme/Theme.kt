package com.mohammed.rickandmorty.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RamDarkColorScheme = darkColorScheme(
    primary = PortalBlue,
    onPrimary = RamOnAccent,
    primaryContainer = PortalBlue,
    onPrimaryContainer = RamOnAccent,
    secondary = PortalGreen,
    onSecondary = RamOnAccent,
    tertiary = PortalGlow,
    onTertiary = RamOnAccent,
    background = RamBackground,
    onBackground = RamTextPrimary,
    surface = RamSurface,
    onSurface = RamTextPrimary,
    surfaceVariant = RamSurfaceElevated,
    onSurfaceVariant = RamTextSecondary,
    surfaceContainer = RamSurface,
    surfaceContainerHigh = RamSurfaceElevated,
    outline = RamOutline,
    outlineVariant = RamOutline,
    scrim = RamScrim
)

private val RamLightColorScheme = lightColorScheme(
    primary = Color(0xFF007C8A),
    onPrimary = Color.White,
    primaryContainer = PortalBlue,
    onPrimaryContainer = Color(0xFF00201F),
    secondary = Color(0xFF5E7A1E),
    onSecondary = Color.White,
    tertiary = Color(0xFF6B7F14),
    onTertiary = Color.White,
    background = Color(0xFFF3F5F8),
    onBackground = Color(0xFF1B1E24),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1B1E24),
    surfaceVariant = Color(0xFFE7EAEF),
    onSurfaceVariant = Color(0xFF44474E),
    surfaceContainer = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFEDEFF3),
    outline = Color(0xFF9AA0A8),
    outlineVariant = Color(0xFFD3D7DD),
    scrim = Color(0x99000000)
)

@Composable
fun RickAndMortyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) RamDarkColorScheme else RamLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
