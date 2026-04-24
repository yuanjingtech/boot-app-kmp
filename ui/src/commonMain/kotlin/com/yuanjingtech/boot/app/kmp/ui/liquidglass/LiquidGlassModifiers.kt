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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Applies a layered semi-transparent gradient background and subtle border
 * to simulate a glass surface — mimicking iOS VisualEffectView using Compose primitives.
 *
 * Uses [isSystemInDarkTheme] internally so the surface adapts automatically
 * to the platform's dark/light mode without depending on shared module types.
 */
@Composable
fun Modifier.liquidGlassSurface(
    cornerRadius: Dp = 16.dp,
    borderAlpha: Float = 0.25f,
): Modifier {
    val isDark = isSystemInDarkTheme()
    return this
        .clip(RoundedCornerShape(cornerRadius))
        .background(
            brush = Brush.verticalGradient(
                colors = if (isDark) {
                    listOf(
                        Color.White.copy(alpha = 0.16f),
                        Color.White.copy(alpha = 0.06f),
                    )
                } else {
                    listOf(
                        Color.White.copy(alpha = 0.22f),
                        Color.White.copy(alpha = 0.08f),
                    )
                }
            ),
            shape = RoundedCornerShape(cornerRadius)
        )
        .border(
            width = 0.5.dp,
            color = Color.White.copy(alpha = borderAlpha),
            shape = RoundedCornerShape(cornerRadius)
        )
}

/**
 * Wrapper composable that provides LiquidGlass color context via [LocalLiquidGlassColors].
 * Inner components can use these colors for text, icons, etc.
 */
@Composable
fun LiquidGlassContainer(
    modifier: Modifier = Modifier,
    colors: LiquidGlassColors = LiquidGlassColors(),
    content: @Composable () -> Unit,
) {
    androidx.compose.runtime.CompositionLocalProvider(
        LocalLiquidGlassColors provides colors
    ) {
        androidx.compose.foundation.layout.Box(modifier = modifier) {
            content()
        }
    }
}
