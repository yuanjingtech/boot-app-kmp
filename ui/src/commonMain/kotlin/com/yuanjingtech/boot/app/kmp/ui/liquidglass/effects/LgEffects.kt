package com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Configuration for LiquidGlass backdrop effects.
 * Based on backdrop library: effects order = color filter → blur → lens
 *
 * @param blurRadius Gaussian blur radius in dp (0 = no blur)
 * @param lensRefractionHeight Lens refraction height in dp (0 = no lens)
 * @param lensRefractionAmount Lens refraction pixel displacement
 * @param lensChromaticAberration Enable RGB color separation in lens
 * @param lensDepthEffect Enable depth shading in lens
 * @param vibrancy Enable iOS-style saturation boost
 * @param surfaceColor Color drawn on top of glass for readability (null = no surface)
 * @param surfaceAlpha Opacity of surface overlay
 */
data class LgEffectConfig(
    val blurRadius: Dp = 4.dp,
    val lensRefractionHeight: Dp = 16.dp,
    val lensRefractionAmount: Dp = 32.dp,
    val lensChromaticAberration: Boolean = false,
    val lensDepthEffect: Boolean = false,
    val vibrancy: Boolean = true,
    val surfaceColor: Color? = Color.White.copy(alpha = 0.5f),
    val surfaceAlpha: Float = 0.5f,
) {
    val hasLens: Boolean get() = lensRefractionHeight > 0.dp
}

/**
 * Standard glass bar effects — used for bottom nav, top app bar.
 */
@Composable
fun rememberLgBarEffects() = remember {
    LgEffectConfig(
        blurRadius = 4.dp,
        lensRefractionHeight = 12.dp,
        lensRefractionAmount = 24.dp,
        lensChromaticAberration = true,
        vibrancy = true,
        surfaceColor = Color.White.copy(alpha = 0.5f),
    )
}

/**
 * Standard glass card effects — stronger blur, no lens.
 */
@Composable
fun rememberLgCardEffects() = remember {
    LgEffectConfig(
        blurRadius = 8.dp,
        lensRefractionHeight = 0.dp,
        vibrancy = true,
        surfaceColor = Color.White.copy(alpha = 0.3f),
    )
}

/**
 * Bottom sheet effects — large lens for dramatic refraction.
 */
@Composable
fun rememberLgBottomSheetEffects() = remember {
    LgEffectConfig(
        blurRadius = 4.dp,
        lensRefractionHeight = 24.dp,
        lensRefractionAmount = 48.dp,
        lensChromaticAberration = true,
        lensDepthEffect = true,
        vibrancy = true,
        surfaceColor = Color.White.copy(alpha = 0.5f),
    )
}

/**
 * Minimal glass effects — blur only, no lens.
 */
@Composable
fun rememberLgMinimalEffects() = remember {
    LgEffectConfig(
        blurRadius = 6.dp,
        lensRefractionHeight = 0.dp,
        vibrancy = false,
        surfaceColor = null,
    )
}

/**
 * @deprecated Use [rememberLgEffects] instead.
 */
@Deprecated("Renamed to rememberLgEffects", ReplaceWith("rememberLgEffects()"))
@Composable
fun rememberLiquidGlassEffects() = rememberLgEffects()

/**
 * Default glass effects — blur + lens + vibrancy + surface.
 */
@Composable
fun rememberLgEffects() = rememberLgBarEffects()
