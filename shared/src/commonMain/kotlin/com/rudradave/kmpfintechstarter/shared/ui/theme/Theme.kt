package com.rudradave.kmpfintechstarter.shared.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/** Applies the app-wide Material 3 theme. */
@Composable
fun FintechStarterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) FintechDarkColorScheme else FintechLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FintechTypography,
        content = content,
    )
}

/** Legacy alias for the theme implementation. */
@Composable
fun FintechAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    FintechStarterTheme(darkTheme = darkTheme, content = content)
}
