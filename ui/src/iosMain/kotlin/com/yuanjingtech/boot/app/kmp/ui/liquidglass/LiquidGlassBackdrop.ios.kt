package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kashif_e.backdrop.Backdrop
import com.kashif_e.backdrop.backdrops.CanvasBackdrop
import com.kashif_e.backdrop.backdrops.rememberLayerBackdrop
import com.kashif_e.backdrop.drawBackdrop
import com.kashif_e.backdrop.effects.blur
import com.kashif_e.backdrop.effects.lens
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig

@Composable
actual fun rememberLiquidGlassBackdrop(
    cornerRadius: Dp,
): LiquidGlassBackdrop {
    val isDark = isSystemInDarkTheme()
    val colors = remember(isDark) {
        if (isDark) {
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
    }
    val backdrop = rememberLayerBackdrop {
        drawRect(brush = Brush.verticalGradient(colors))
    }
    return LiquidGlassBackdrop(native = backdrop)
}

@Composable
actual fun Modifier.liquidGlassBackdrop(
    backdrop: LiquidGlassBackdrop,
    cornerRadius: Dp,
    borderAlpha: Float,
    config: LgEffectConfig,
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)
    val native = backdrop.native as? Backdrop ?: return this
    return this
        .drawBackdrop(
            backdrop = native,
            shape = { shape },
            effects = {
                // NOTE: vibrancy() is intentionally skipped on iOS — backdrop
                // 0.0.1-alpha02 routes through org.jetbrains.skia.ColorMatrix,
                // which fails to link on iOS KMP (IrLinkageError). Other effects
                // (blur, lens) are safe. Glass readability is preserved by the
                // surfaceColor/surfaceAlpha overlay.
                if (config.blurRadius > 0.dp) blur(config.blurRadius.toPx())
                if (config.hasLens) lens(
                    refractionHeight = config.lensRefractionHeight.toPx(),
                    refractionAmount = config.lensRefractionAmount.toPx(),
                    depthEffect = config.lensDepthEffect,
                    chromaticAberration = config.lensChromaticAberration,
                )
            },
        )
        .clip(shape)
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
): Modifier {
    return if (backdrop != null) {
        liquidGlassBackdrop(backdrop, cornerRadius, borderAlpha, config)
    } else {
        liquidGlassSurface(cornerRadius, borderAlpha)
    }
}

@Composable
actual fun Modifier.liquidGlassBackdropCanvas(
    onDraw: DrawScope.() -> Unit,
    cornerRadius: Dp,
    borderAlpha: Float,
    config: LgEffectConfig,
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)
    val canvasBackdrop = CanvasBackdrop(onDraw)
    return this
        .drawBackdrop(
            backdrop = canvasBackdrop,
            shape = { shape },
            effects = {
                // vibrancy() skipped on iOS — see comment in liquidGlassBackdrop.
                if (config.blurRadius > 0.dp) blur(config.blurRadius.toPx())
                if (config.hasLens) lens(
                    refractionHeight = config.lensRefractionHeight.toPx(),
                    refractionAmount = config.lensRefractionAmount.toPx(),
                    depthEffect = config.lensDepthEffect,
                    chromaticAberration = config.lensChromaticAberration,
                )
            },
        )
        .clip(shape)
        .border(
            width = 0.5.dp,
            color = Color.White.copy(alpha = borderAlpha),
            shape = shape,
        )
}