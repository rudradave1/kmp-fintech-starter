package com.rudradave.kmpfintechstarter.android.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val FintechLightColorScheme = lightColorScheme(
    primary = SeedPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8E6FF),
    onPrimaryContainer = Color(0xFF2A225A),
    secondary = SeedSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFA8FFF7),
    onSecondaryContainer = Color(0xFF032B29),
    background = Color(0xFFFBFBFF),
    onBackground = Color(0xFF0A1020),
    surface = Color.White,
    onSurface = Color(0xFF0A1020),
    surfaceVariant = Color(0xFFF1F4FF),
    onSurfaceVariant = Color(0xFF505B7A),
)

/** Applies the app-wide Material 3 theme with dynamic color when available. */
@Composable
fun FintechStarterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> FintechDarkColorScheme
        else -> FintechLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FintechTypography,
        content = content,
    )
}
