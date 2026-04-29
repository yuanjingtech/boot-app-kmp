package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

// ─── LiquidGlass Colors ──────────────────────────────────────────────────────

/**
 * Color palette for LiquidGlass components supporting light/dark mode.
 *
 * Design principle: glassmorphism works by having a semi-transparent overlay
 * on top of a colorful background. The overlay color should contrast with the
 * background so the glass edge is visible.
 */
data class LiquidGlassColors(
    // Content colors (text/icons on the glass surface)
    val content: Color,
    val contentAlpha: Float,
    val secondary: Color,
    val secondaryAlpha: Float,
    val disabled: Color,
    val disabledAlpha: Float,

    // Surface colors (the glass overlay itself)
    val surface: Color,
    val surfaceAlpha: Float,
    val surfaceBorder: Color,
    val surfaceBorderAlpha: Float,

    // Interactive control colors
    val checkedFill: Color,
    val checkedFillAlpha: Float,
    val thumb: Color,
) {
    companion object {
        // ─── Dark mode (default) ──────────────────────────────────────────────
        val Dark = LiquidGlassColors(
            content = Color.White,
            contentAlpha = 0.92f,
            secondary = Color.White,
            secondaryAlpha = 0.60f,
            disabled = Color.White,
            disabledAlpha = 0.38f,
            surface = Color.White,
            surfaceAlpha = 0.50f,
            surfaceBorder = Color.White,
            surfaceBorderAlpha = 0.25f,
            checkedFill = Color.White,
            checkedFillAlpha = 0.30f,
            thumb = Color.White,
        )

        // ─── Light mode ───────────────────────────────────────────────────────
        val Light = LiquidGlassColors(
            content = Color(0xFF1C1B1F),
            contentAlpha = 0.90f,
            secondary = Color(0xFF1C1B1F),
            secondaryAlpha = 0.60f,
            disabled = Color(0xFF1C1B1F),
            disabledAlpha = 0.38f,
            surface = Color(0xFF1C1B1F),
            surfaceAlpha = 0.08f,
            surfaceBorder = Color(0xFF1C1B1F),
            surfaceBorderAlpha = 0.18f,
            checkedFill = Color(0xFF1C1B1F),
            checkedFillAlpha = 0.12f,
            thumb = Color(0xFF1C1B1F),
        )
    }
}

// ─── CompositionLocal ────────────────────────────────────────────────────────

val LocalLiquidGlassColors = compositionLocalOf { LiquidGlassColors.Dark }

// ─── Auto-detecting provider ─────────────────────────────────────────────────

/**
 * Provides LiquidGlassColors based on the current [MaterialTheme.colorScheme].
 * Uses the background surface luminance to detect if we're in light or dark mode.
 */
@Composable
fun LiquidGlassTheme(
    content: @Composable () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val isLight = scheme.surface.luminance() > 0.5f
    val colors = if (isLight) LiquidGlassColors.Light else LiquidGlassColors.Dark
    CompositionLocalProvider(LocalLiquidGlassColors provides colors) {
        content()
    }
}
