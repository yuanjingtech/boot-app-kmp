package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * LiquidGlass color palette — semi-transparent glass surfaces with subtle borders.
 * Designed to mimic iOS VisualEffectView aesthetic using Compose primitives.
 */
data class LiquidGlassColors(
    val surface: Color = Color.White.copy(alpha = 0.18f),
    val surfaceDark: Color = Color.White.copy(alpha = 0.10f),
    val border: Color = Color.White.copy(alpha = 0.25f),
    val content: Color = Color.White,
    val contentDark: Color = Color.White.copy(alpha = 0.87f),
)

val LocalLiquidGlassColors = compositionLocalOf { LiquidGlassColors() }
