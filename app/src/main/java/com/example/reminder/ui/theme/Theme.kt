package com.example.reminder.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import com.example.reminder.R
import com.example.reminder.screens.AppThemePalette

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFaac7ff), onPrimary = Color(0xFF0a305f),
    secondary = Color(0xFFbec6dc), onSecondary = Color(0xFF283141),
    secondaryContainer = Color(0xFF3e4759), onSecondaryContainer = Color(0xFFdae2f9),
    tertiary = Color(0xFFddbce0), onTertiary = Color(0xFF3f2844),
    surface = Color(0xFF111318), onSurface = Color(0xFFe2e2e9),
    surfaceBright = Color(0xFF37393e), error = Color(0xFFffb4ab), onError = Color(0xFF690005),
    onSurfaceVariant = Color(0xFFc4c6d0), surfaceContainerLow = Color(0xFF191c20)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF415f91), onPrimary = Color.White,
    secondary = Color(0xFF565f71), onSecondary = Color.White,
    secondaryContainer = Color(0xFFdae2f9), onSecondaryContainer = Color(0xFF3e4759),
    tertiary = Color(0xFF705575), onTertiary = Color.White,
    onTertiaryContainer = Color(0xFF573e5c), error = Color(0xFFba1a1a),
    onErrorContainer = Color(0xFFffdad6), surface = Color(0xFFf9f9ff),
    onSurface = Color(0xFF191c20), surfaceBright = Color(0xFFd9d9e0),
    onSurfaceVariant = Color(0xFF191c20), surfaceContainerLow = Color(0xFFf3f3fa)
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    palette: AppThemePalette? = null,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val baseScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val colorScheme = if (palette == null) baseScheme else baseScheme.copy(
        primary = palette.primary, onPrimary = palette.onPrimary,
        secondary = palette.secondary, onSecondary = palette.onSecondary,
        tertiary = palette.tertiary, onTertiary = palette.onTeriary
    )
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
