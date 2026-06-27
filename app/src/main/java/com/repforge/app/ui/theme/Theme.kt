package com.repforge.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricGreen,
    secondary = NeonBlue,
    tertiary = NeonPurple,
    background = DeepBlack,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    primaryContainer = DarkSurfaceVariant,
    secondaryContainer = DarkSurfaceVariant,
    tertiaryContainer = DarkSurfaceVariant,
    onPrimary = DeepBlack,
    onSecondary = DeepBlack,
    onTertiary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite,
    onSurfaceVariant = TextWhite
)

private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    secondary = NeonBlue,
    tertiary = NeonPurple,
    background = CrispWhite,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    primaryContainer = LightSurfaceVariant,
    secondaryContainer = LightSurfaceVariant,
    tertiaryContainer = LightSurfaceVariant,
    onPrimary = CrispWhite,
    onSecondary = TextBlack,
    onTertiary = TextWhite,
    onBackground = TextBlack,
    onSurface = TextBlack,
    onSurfaceVariant = TextBlack
)

@Composable
fun RepForgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
