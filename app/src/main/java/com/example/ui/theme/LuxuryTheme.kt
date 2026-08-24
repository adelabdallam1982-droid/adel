package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class LuxuryThemePreset(
  val id: String,
  val nameAr: String,
  val subtitleAr: String,
  val previewPrimary: Color,
  val previewSecondary: Color,
  val previewBackground: Color
) {
  ROYAL_MIDNIGHT_GOLD(
    id = "midnight_gold",
    nameAr = "الذهب الملكي والليلي",
    subtitleAr = "أصالة الليل المخملي مع بريق الذهب الخالص",
    previewPrimary = Color(0xFFF59E0B),
    previewSecondary = Color(0xFFFBBF24),
    previewBackground = Color(0xFF060911)
  ),
  PROPHETIC_EMERALD(
    id = "prophetic_emerald",
    nameAr = "الزمرد النبوي والذهب",
    subtitleAr = "خضرة الروضة الشريفة مع الإشراقة الذهبية",
    previewPrimary = Color(0xFF10B981),
    previewSecondary = Color(0xFFEAB308),
    previewBackground = Color(0xFF021B14)
  ),
  TAIF_RUBY(
    id = "taif_ruby",
    nameAr = "الياقوت العنابي والورد",
    subtitleAr = "دفء الورد الطائفي الفاخر مع الذهب الوردي",
    previewPrimary = Color(0xFFF43F5E),
    previewSecondary = Color(0xFFFBBF24),
    previewBackground = Color(0xFF19040A)
  ),
  ANDALUSIAN_TURQUOISE(
    id = "andalusian_turquoise",
    nameAr = "الفيروز الأندلسي والرمال",
    subtitleAr = "سحر الفيروز السماوي مع نفحات رمال مكة",
    previewPrimary = Color(0xFF06B6D4),
    previewSecondary = Color(0xFFF59E0B),
    previewBackground = Color(0xFF021A20)
  ),
  ROYAL_IVORY(
    id = "royal_ivory",
    nameAr = "الرخام الأبيض والذهب النقي",
    subtitleAr = "بياض رخام الحرم الشريف مع لمعان الذهب العربي",
    previewPrimary = Color(0xFFB45309),
    previewSecondary = Color(0xFFD97706),
    previewBackground = Color(0xFFFBF9F5)
  )
}

data class LuxuryColors(
  val preset: LuxuryThemePreset,
  val primary: Color,
  val primaryVariant: Color,
  val secondary: Color,
  val background: Color,
  val headerBackground: Color,
  val surface: Color,
  val surfaceElevated: Color,
  val surfaceSubtle: Color,
  val border: Color,
  val borderHighlight: Color,
  val textPrimary: Color,
  val textSecondary: Color,
  val textMuted: Color,
  val textOnPrimary: Color,
  val accentBadge: Color,
  val isLight: Boolean = false
) {
  val primaryGradient: Brush
    get() = Brush.linearGradient(listOf(primary, primaryVariant))

  val cardGradient: Brush
    get() = Brush.verticalGradient(
      listOf(
        surfaceElevated.copy(alpha = 0.95f),
        surface.copy(alpha = 0.98f)
      )
    )

  val goldAccentGradient: Brush
    get() = Brush.linearGradient(
      listOf(
        Color(0xFFFDE68A),
        primaryVariant,
        primary
      )
    )
}

fun getLuxuryColors(preset: LuxuryThemePreset): LuxuryColors {
  return when (preset) {
    LuxuryThemePreset.ROYAL_MIDNIGHT_GOLD -> LuxuryColors(
      preset = preset,
      primary = Color(0xFFF59E0B),
      primaryVariant = Color(0xFFFBBF24),
      secondary = Color(0xFF10B981),
      background = Color(0xFF050811),
      headerBackground = Color(0xFF0A101D),
      surface = Color(0xFF0F172A),
      surfaceElevated = Color(0xFF1E293B),
      surfaceSubtle = Color(0xFF131D31),
      border = Color(0xFF1E293B),
      borderHighlight = Color(0xFF334155),
      textPrimary = Color(0xFFF8FAFC),
      textSecondary = Color(0xFF94A3B8),
      textMuted = Color(0xFF64748B),
      textOnPrimary = Color(0xFF050811),
      accentBadge = Color(0xFFF59E0B),
      isLight = false
    )

    LuxuryThemePreset.PROPHETIC_EMERALD -> LuxuryColors(
      preset = preset,
      primary = Color(0xFF10B981),
      primaryVariant = Color(0xFF34D399),
      secondary = Color(0xFFEAB308),
      background = Color(0xFF021B14),
      headerBackground = Color(0xFF052B20),
      surface = Color(0xFF08382B),
      surfaceElevated = Color(0xFF0F4D3C),
      surfaceSubtle = Color(0xFF073024),
      border = Color(0xFF0F4D3C),
      borderHighlight = Color(0xFF1B6B54),
      textPrimary = Color(0xFFF0FDF4),
      textSecondary = Color(0xFFA7F3D0),
      textMuted = Color(0xFF6EE7B7),
      textOnPrimary = Color(0xFF021B14),
      accentBadge = Color(0xFFFACC15),
      isLight = false
    )

    LuxuryThemePreset.TAIF_RUBY -> LuxuryColors(
      preset = preset,
      primary = Color(0xFFF43F5E),
      primaryVariant = Color(0xFFFB7185),
      secondary = Color(0xFFFBBF24),
      background = Color(0xFF180309),
      headerBackground = Color(0xFF270611),
      surface = Color(0xFF350B19),
      surfaceElevated = Color(0xFF4C1024),
      surfaceSubtle = Color(0xFF2E0915),
      border = Color(0xFF4C1024),
      borderHighlight = Color(0xFF701A36),
      textPrimary = Color(0xFFFFF1F2),
      textSecondary = Color(0xFFFECDD3),
      textMuted = Color(0xFFFDA4AF),
      textOnPrimary = Color(0xFF180309),
      accentBadge = Color(0xFFFBBF24),
      isLight = false
    )

    LuxuryThemePreset.ANDALUSIAN_TURQUOISE -> LuxuryColors(
      preset = preset,
      primary = Color(0xFF06B6D4),
      primaryVariant = Color(0xFF22D3EE),
      secondary = Color(0xFFF59E0B),
      background = Color(0xFF02191F),
      headerBackground = Color(0xFF052831),
      surface = Color(0xFF093945),
      surfaceElevated = Color(0xFF0F4F5F),
      surfaceSubtle = Color(0xFF07313B),
      border = Color(0xFF0F4F5F),
      borderHighlight = Color(0xFF176E84),
      textPrimary = Color(0xFFECFEFF),
      textSecondary = Color(0xFFA5F3FC),
      textMuted = Color(0xFF67E8F9),
      textOnPrimary = Color(0xFF02191F),
      accentBadge = Color(0xFFF59E0B),
      isLight = false
    )

    LuxuryThemePreset.ROYAL_IVORY -> LuxuryColors(
      preset = preset,
      primary = Color(0xFFB45309),
      primaryVariant = Color(0xFFD97706),
      secondary = Color(0xFF059669),
      background = Color(0xFFF8F5EE),
      headerBackground = Color(0xFFEFE8DB),
      surface = Color(0xFFFFFFFF),
      surfaceElevated = Color(0xFFF3EDE0),
      surfaceSubtle = Color(0xFFFAF7F2),
      border = Color(0xFFE2D7C3),
      borderHighlight = Color(0xFFC7B79E),
      textPrimary = Color(0xFF1C1917),
      textSecondary = Color(0xFF44403C),
      textMuted = Color(0xFF78716C),
      textOnPrimary = Color(0xFFFFFFFF),
      accentBadge = Color(0xFFD97706),
      isLight = true
    )
  }
}

val LocalLuxuryColors = compositionLocalOf { getLuxuryColors(LuxuryThemePreset.ROYAL_MIDNIGHT_GOLD) }

object LuxuryTheme {
  val colors: LuxuryColors
    @Composable
    @ReadOnlyComposable
    get() = LocalLuxuryColors.current
}
