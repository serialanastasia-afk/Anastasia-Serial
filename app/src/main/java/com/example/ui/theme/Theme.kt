package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val DarkColorScheme = darkColorScheme(
    primary = ChampionshipGold,
    onPrimary = RoyalNavyDark,
    primaryContainer = RoyalNavyCard,
    onPrimaryContainer = ChampionshipGoldLight,
    secondary = NileTealDarkSecondary,
    onSecondary = RoyalNavyDark,
    secondaryContainer = Color(0xFF134E4A),
    onSecondaryContainer = Color(0xFFCCFBF1),
    tertiary = WarmAmberDarkTertiary,
    onTertiary = RoyalNavyDark,
    background = RoyalNavyDark,
    onBackground = Color(0xFFF8FAFC),
    surface = RoyalNavySurface,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = RoyalNavyCard,
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = WrongCoral
)

private val LightColorScheme = lightColorScheme(
    primary = RoyalBluePrimary,
    onPrimary = Color.White,
    primaryContainer = RoyalBlueContainer,
    onPrimaryContainer = OnRoyalBlueContainer,
    secondary = NileTealSecondary,
    onSecondary = Color.White,
    secondaryContainer = NileTealContainer,
    onSecondaryContainer = OnNileTealContainer,
    tertiary = WarmAmberTertiary,
    onTertiary = Color.White,
    tertiaryContainer = ChampionshipGoldContainer,
    onTertiaryContainer = OnChampionshipGoldContainer,
    background = LightBackground,
    onBackground = OnLightText,
    surface = LightSurface,
    onSurface = OnLightText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = OnLightSubtext,
    error = WrongCoral
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
