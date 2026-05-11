package com.rudradave.kmpfintechstarter.shared.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

val SeedPrimary = Color(0xFF6C63FF)
val SeedSecondary = Color(0xFF03DAC6)

val FintechDarkColorScheme = darkColorScheme(
    primary = SeedPrimary,
    onPrimary = Color(0xFFF8F8FF),
    primaryContainer = Color(0xFF2A225A),
    onPrimaryContainer = Color(0xFFE8E6FF),
    secondary = SeedSecondary,
    onSecondary = Color(0xFF032B29),
    secondaryContainer = Color(0xFF0A3E3A),
    onSecondaryContainer = Color(0xFFA8FFF7),
    tertiary = Color(0xFFFF7AB6),
    onTertiary = Color(0xFF3F1028),
    background = Color(0xFF0A1020),
    onBackground = Color(0xFFF1F4FF),
    surface = Color(0xFF121A2D),
    onSurface = Color(0xFFF4F6FB),
    surfaceVariant = Color(0xFF232B40),
    onSurfaceVariant = Color(0xFFB8C0D9),
    outline = Color(0xFF505B7A),
    outlineVariant = Color(0xFF323B56),
    error = Color(0xFFFF6E6E),
    onError = Color(0xFF3A0008),
)

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

val CategoryFood = Color(0xFFFFA54D)
val CategoryTransport = Color(0xFF5DA9FF)
val CategoryEntertainment = Color(0xFF9D7BFF)
val CategoryShopping = Color(0xFFFF78C7)
val CategoryHealth = Color(0xFFFF6F91)
val CategoryUtilities = Color(0xFF24C5B2)
val CategoryTransfer = Color(0xFF43C97B)

val StatusCompleted = Color(0xFF4FCB81)
val StatusPending = Color(0xFFFFC857)
val StatusFailed = Color(0xFFFF6B6B)
val StatusRefunded = Color(0xFF63A8FF)
