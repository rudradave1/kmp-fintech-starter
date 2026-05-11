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


import com.rudradave.kmpfintechstarter.shared.ui.theme.FintechAppTheme

/** Applies the app-wide Material 3 theme. Android-specific dynamic color removed to keep shared theme consistent. */
@Composable
fun FintechStarterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    FintechAppTheme(darkTheme = darkTheme, content = content)
}
