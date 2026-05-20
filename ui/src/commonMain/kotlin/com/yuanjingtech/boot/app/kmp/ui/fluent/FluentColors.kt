package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.ui.graphics.Color

/**
 * Color palette for Fluent Design components supporting light/dark mode.
 *
 * Based on Windows 11 Fluent Design System color system with accent-based theming.
 * Uses the Windows 11 default light/dark palettes.
 */
data class FluentColors(
    // Background colors
    val background: Color,
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceElevated: Color,

    // Text colors
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
    val textOnAccent: Color,

    // Border colors
    val borderDefault: Color,
    val borderSecondary: Color,
    val borderStrong: Color,

    // Interactive colors
    val accent: Color,
    val accentLight1: Color,
    val accentLight2: Color,
    val accentDark1: Color,
    val accentDark2: Color,
    val accentText: Color,

    // Control colors
    val controlFill: Color,
    val controlFillSecondary: Color,
    val controlFillTertiary: Color,
    val controlFillDisabled: Color,
    val controlStroke: Color,
    val controlStrokeDefault: Color,
    val controlStrokeStrong: Color,
    val controlStrokeDisabled: Color,

    // Status colors
    val success: Color,
    val warning: Color,
    val error: Color,
    val info: Color,
) {
    companion object {
        // ─── Dark mode (Windows 11 Dark) ───────────────────────────────────────
        val Dark = FluentColors(
            background = Color(0xFF202020),
            backgroundSecondary = Color(0xFF2D2D2D),
            backgroundTertiary = Color(0xFF383838),
            surface = Color(0xFF2D2D2D),
            surfaceVariant = Color(0xFF383838),
            surfaceElevated = Color(0xFF383838),

            textPrimary = Color.White,
            textSecondary = Color(0xFFB0B0B0),
            textTertiary = Color(0xFF808080),
            textDisabled = Color(0xFF5C5C5C),
            textOnAccent = Color.White,

            borderDefault = Color(0xFF3D3D3D),
            borderSecondary = Color(0xFF2D2D2D),
            borderStrong = Color(0xFF606060),

            accent = Color(0xFF60CDFF),
            accentLight1 = Color(0xFF8FDBFF),
            accentLight2 = Color(0xFFB5E7FF),
            accentDark1 = Color(0xFF3AA6DB),
            accentDark2 = Color(0xFF0078D4),
            accentText = Color.White,

            controlFill = Color(0xFF3D3D3D),
            controlFillSecondary = Color(0xFF2D2D2D),
            controlFillTertiary = Color(0xFF252525),
            controlFillDisabled = Color(0xFF1A1A1A),
            controlStroke = Color(0xFF6B6B6B),
            controlStrokeDefault = Color(0xFF454545),
            controlStrokeStrong = Color(0xFF8A8A8A),
            controlStrokeDisabled = Color(0xFF3D3D3D),

            success = Color(0xFF6CCB5F),
            warning = Color(0xFFFCE100),
            error = Color(0xFFF85149),
            info = Color(0xFF60CDFF),
        )

        // ─── Light mode (Windows 11 Light) ─────────────────────────────────────
        val Light = FluentColors(
            background = Color(0xFFF3F3F3),
            backgroundSecondary = Color(0xFFFFFFFF),
            backgroundTertiary = Color(0xFFF9F9F9),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFF3F3F3),
            surfaceElevated = Color(0xFFFFFFFF),

            textPrimary = Color(0xFF1A1A1A),
            textSecondary = Color(0xFF616161),
            textTertiary = Color(0xFF8A8A8A),
            textDisabled = Color(0xFFADADAD),
            textOnAccent = Color.White,

            borderDefault = Color(0xFFE5E5E5),
            borderSecondary = Color(0xFFEDEDED),
            borderStrong = Color(0xFFC4C4C4),

            accent = Color(0xFF0078D4),
            accentLight1 = Color(0xFF429CE3),
            accentLight2 = Color(0xFF60CDFF),
            accentDark1 = Color(0xFF005A9E),
            accentDark2 = Color(0xFF004C87),
            accentText = Color.White,

            controlFill = Color(0xFFFFFFFF),
            controlFillSecondary = Color(0xFFF5F5F5),
            controlFillTertiary = Color(0xFFEBEBEB),
            controlFillDisabled = Color(0xFFF5F5F5),
            controlStroke = Color(0xFF8A8A8A),
            controlStrokeDefault = Color(0xFFDDDDDD),
            controlStrokeStrong = Color(0xFF6B6B6B),
            controlStrokeDisabled = Color(0xFFE5E5E5),

            success = Color(0xFF107C10),
            warning = Color(0xFFFFB900),
            error = Color(0xFFC42B1C),
            info = Color(0xFF0078D4),
        )
    }
}