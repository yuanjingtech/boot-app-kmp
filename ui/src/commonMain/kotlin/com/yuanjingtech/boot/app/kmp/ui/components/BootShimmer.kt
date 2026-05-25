package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle

/**
 * Shimmer skeleton loading colors for different UI styles.
 *
 * The shimmer effect uses a linear gradient that sweeps across the surface,
 * revealing a lighter highlight over the base color.
 */
private data class ShimmerColors(
    val baseColor: Color,
    val highlightColor: Color,
)

/**
 * Returns shimmer colors based on the current UI style and theme.
 */
@Composable
private fun shimmerColors(
    style: BootUiStyle,
    isDarkTheme: Boolean,
): ShimmerColors {
    return when (style) {
        BootUiStyle.LIQUID_GLASS -> if (isDarkTheme) {
            ShimmerColors(
                baseColor = Color(0xFF2C2C2C),
                highlightColor = Color(0xFF3A3A3A),
            )
        } else {
            ShimmerColors(
                baseColor = Color(0xFFE8E8E8),
                highlightColor = Color(0xFFF5F5F5),
            )
        }
        BootUiStyle.MATERIAL3 -> if (isDarkTheme) {
            ShimmerColors(
                baseColor = Color(0xFF2C2C2C),
                highlightColor = Color(0xFF3A3A3A),
            )
        } else {
            ShimmerColors(
                baseColor = Color(0xFFE0E0E0),
                highlightColor = Color(0xFFF5F5F5),
            )
        }
        BootUiStyle.FLUENT -> if (isDarkTheme) {
            ShimmerColors(
                baseColor = Color(0xFF2D2D2D),
                highlightColor = Color(0xFF3D3D3D),
            )
        } else {
            ShimmerColors(
                baseColor = Color(0xFFE4E4E4),
                highlightColor = Color(0xFFF0F0F0),
            )
        }
    }
}

/**
 * Applies a shimmer skeleton loading effect to the modifier.
 *
 * When [visible] is true, shows an animated shimmer effect using the provided colors.
 * When [visible] is false, returns the modifier unchanged.
 *
 * The shimmer creates a light sweep animation from left to right using a 5-stop
 * gradient, which works consistently across all Compose targets (Android, iOS, JVM, Web/WASM).
 *
 * @param visible Whether to show the shimmer loading effect
 * @param cornerRadius Corner radius for clipping the shimmer area
 * @param baseColor Base color of the skeleton (defaults to style-aware color)
 * @param highlightColor Highlight color for the shimmer sweep (defaults to style-aware color)
 */
@Composable
fun Modifier.shimmerEffect(
    visible: Boolean,
    cornerRadius: Dp = 8.dp,
    baseColor: Color? = null,
    highlightColor: Color? = null,
): Modifier {
    if (!visible) return this

    val style = LocalUiStyle.current
    val isDarkTheme = isSystemInDarkTheme()
    val colors = shimmerColors(style, isDarkTheme)
    val resolvedBase = baseColor ?: colors.baseColor
    val resolvedHighlight = highlightColor ?: colors.highlightColor

    // Animation: sweep from left (0) to right (1) repeatedly
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerOffset",
    )

    // Build gradient offsets based on animation progress
    // Gradient: base -> base -> highlight -> base -> base
    // The highlight window (stops 2-3) sweeps from left (offset=-0.5) to right (offset=1.5)
    val highlightCenter = shimmerOffset
    val highlightLeft = (highlightCenter - 0.4f).coerceIn(-0.5f, 1.0f)
    val highlightRight = (highlightCenter + 0.4f).coerceIn(0.0f, 1.5f)

    val gradientStops = listOf(
        0f to resolvedBase,
        highlightLeft.coerceAtLeast(0f) to resolvedBase,
        highlightCenter.coerceIn(0f, 1f) to resolvedHighlight,
        highlightRight.coerceAtMost(1f) to resolvedHighlight,
        1f to resolvedBase,
    ).sortedBy { it.first }.map { it.second }

    return this
        .clip(RoundedCornerShape(cornerRadius))
        .background(
            brush = Brush.horizontalGradient(gradientStops),
            shape = RoundedCornerShape(cornerRadius),
        )
}

/**
 * High-level shimmer modifier that uses style-aware colors.
 * Convenience wrapper for [shimmerEffect] with default shimmer colors.
 *
 * @param visible Whether to show the shimmer loading effect
 * @param cornerRadius Corner radius for clipping the shimmer area
 */
@Composable
fun Modifier.shimmerLoading(
    visible: Boolean,
    cornerRadius: Dp = 8.dp,
): Modifier = shimmerEffect(
    visible = visible,
    cornerRadius = cornerRadius,
)