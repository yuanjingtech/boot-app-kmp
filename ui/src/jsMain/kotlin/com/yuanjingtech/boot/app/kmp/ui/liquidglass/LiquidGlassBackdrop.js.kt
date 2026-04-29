package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig

@Composable
actual fun rememberLiquidGlassBackdrop(
    cornerRadius: Dp,
): LiquidGlassBackdrop = LiquidGlassBackdrop(native = null)

@Composable
actual fun Modifier.liquidGlassBackdrop(
    backdrop: LiquidGlassBackdrop,
    cornerRadius: Dp,
    borderAlpha: Float,
    config: LgEffectConfig,
): Modifier {
    val isDark = isSystemInDarkTheme()
    val shape = RoundedCornerShape(cornerRadius)
    return this
        .clip(shape)
        .background(
            brush = Brush.verticalGradient(
                colors = if (isDark) {
                    listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color.White.copy(alpha = 0.05f),
                    )
                } else {
                    listOf(
                        Color.White.copy(alpha = 0.18f),
                        Color.White.copy(alpha = 0.06f),
                    )
                }
            ),
            shape = shape,
        )
        .border(
            width = 0.5.dp,
            color = Color.White.copy(alpha = borderAlpha),
            shape = shape,
        )
}

@Composable
actual fun Modifier.liquidGlassBackdropOrSurface(
    backdrop: LiquidGlassBackdrop?,
    cornerRadius: Dp,
    borderAlpha: Float,
    config: LgEffectConfig,
): Modifier = liquidGlassSurface(cornerRadius, borderAlpha)

@Composable
actual fun Modifier.liquidGlassBackdropCanvas(
    onDraw: DrawScope.() -> Unit,
    cornerRadius: Dp,
    borderAlpha: Float,
    config: LgEffectConfig,
): Modifier {
    val isDark = isSystemInDarkTheme()
    val shape = RoundedCornerShape(cornerRadius)
    return this
        .clip(shape)
        .background(
            brush = Brush.verticalGradient(
                colors = if (isDark) {
                    listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color.White.copy(alpha = 0.05f),
                    )
                } else {
                    listOf(
                        Color.White.copy(alpha = 0.18f),
                        Color.White.copy(alpha = 0.06f),
                    )
                }
            ),
            shape = shape,
        )
        .border(
            width = 0.5.dp,
            color = Color.White.copy(alpha = borderAlpha),
            shape = shape,
        )
}
