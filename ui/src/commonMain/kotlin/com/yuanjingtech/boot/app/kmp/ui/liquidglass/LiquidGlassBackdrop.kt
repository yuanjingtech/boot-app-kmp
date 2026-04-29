package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig

/**
 * Wrapper for platform-specific backdrop content.
 * - On Android/JVM/iOS/WasmJs: wraps the library's [com.kashif_e.backdrop.Backdrop]
 * - On JS: gradient-only fallback (native = null)
 */
@ConsistentCopyVisibility
data class LiquidGlassBackdrop internal constructor(
    val native: Any?,
)

@Composable
expect fun rememberLiquidGlassBackdrop(
    cornerRadius: Dp = 16.dp,
): LiquidGlassBackdrop

@Composable
expect fun Modifier.liquidGlassBackdrop(
    backdrop: LiquidGlassBackdrop,
    cornerRadius: Dp = 16.dp,
    borderAlpha: Float = 0.25f,
    config: LgEffectConfig = LgEffectConfig(),
): Modifier

@Composable
expect fun Modifier.liquidGlassBackdropOrSurface(
    backdrop: LiquidGlassBackdrop?,
    cornerRadius: Dp = 16.dp,
    borderAlpha: Float = 0.25f,
    config: LgEffectConfig = LgEffectConfig(),
): Modifier

@Composable
expect fun Modifier.liquidGlassBackdropCanvas(
    onDraw: DrawScope.() -> Unit,
    cornerRadius: Dp = 16.dp,
    borderAlpha: Float = 0.25f,
    config: LgEffectConfig = LgEffectConfig(),
): Modifier
