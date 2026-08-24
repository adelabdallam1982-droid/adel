package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun MyApplicationTheme(
  preset: LuxuryThemePreset = LuxuryThemePreset.ROYAL_MIDNIGHT_GOLD,
  content: @Composable () -> Unit,
) {
  val luxuryColors = getLuxuryColors(preset)

  val colorScheme = if (luxuryColors.isLight) {
    lightColorScheme(
      primary = luxuryColors.primary,
      onPrimary = luxuryColors.textOnPrimary,
      primaryContainer = luxuryColors.surfaceElevated,
      onPrimaryContainer = luxuryColors.textPrimary,
      secondary = luxuryColors.secondary,
      onSecondary = Color.White,
      background = luxuryColors.background,
      surface = luxuryColors.surface,
      surfaceVariant = luxuryColors.surfaceElevated,
      onBackground = luxuryColors.textPrimary,
      onSurface = luxuryColors.textPrimary,
      onSurfaceVariant = luxuryColors.textSecondary,
      outline = luxuryColors.border,
      outlineVariant = luxuryColors.borderHighlight
    )
  } else {
    darkColorScheme(
      primary = luxuryColors.primary,
      onPrimary = luxuryColors.textOnPrimary,
      primaryContainer = luxuryColors.surfaceElevated,
      onPrimaryContainer = luxuryColors.primaryVariant,
      secondary = luxuryColors.secondary,
      onSecondary = Color.White,
      background = luxuryColors.background,
      surface = luxuryColors.surface,
      surfaceVariant = luxuryColors.surfaceElevated,
      onBackground = luxuryColors.textPrimary,
      onSurface = luxuryColors.textPrimary,
      onSurfaceVariant = luxuryColors.textSecondary,
      outline = luxuryColors.border,
      outlineVariant = luxuryColors.borderHighlight
    )
  }

  CompositionLocalProvider(LocalLuxuryColors provides luxuryColors) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
  }
}
