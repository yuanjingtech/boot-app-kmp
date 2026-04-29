package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Default values for LiquidGlass effects based on backdrop library.
 *
 * Effects order (per backdrop docs): color filter → blur → lens
 */
object LiquidGlassDefaults {
    // ─── Shape defaults ────────────────────────────────────────────────────────

    val BottomSheetShape: CornerBasedShape = RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp)
    val BottomBarShape: CornerBasedShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    val CardShape: CornerBasedShape = RoundedCornerShape(24.dp)
    val ButtonShape: CornerBasedShape = RoundedCornerShape(16.dp)

    // ─── Blur defaults ────────────────────────────────────────────────────────

    val BlurRadius: Dp = 4.dp
    val StrongBlurRadius: Dp = 12.dp

    // ─── Lens defaults ───────────────────────────────────────────────────────

    val LensRefractionHeight: Dp = 16.dp
    val LensRefractionAmount: Dp = 32.dp
    val BottomSheetLensHeight: Dp = 24.dp
    val BottomSheetLensAmount: Dp = 48.dp

    // ─── Surface overlay ─────────────────────────────────────────────────────

    val SurfaceColor: Color = Color.White.copy(alpha = 0.5f)
    val SurfaceAlpha: Float = 0.5f

    // ─── Border ──────────────────────────────────────────────────────────────

    val BorderAlpha: Float = 0.25f
    val BorderWidth: Dp = 0.5.dp
}
