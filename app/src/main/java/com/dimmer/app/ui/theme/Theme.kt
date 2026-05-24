package com.dimmer.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DimmerColorScheme = darkColorScheme(
    primary = SteelBlue,
    onPrimary = NavyBlack,
    primaryContainer = SteelBlueDim,
    onPrimaryContainer = TextPrimary,
    secondary = TextSecondary,
    onSecondary = NavyBlack,
    background = NavyBlack,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceMid,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = TextPrimary,
)

@Composable
fun DimmerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DimmerColorScheme,
        typography = Typography,
        content = content
    )
}