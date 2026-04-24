package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassSurface
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Surface

/**
 * Boot-styled surface container. Routes to [LiquidGlassSurface] or [Material3Surface]
 * based on [LocalUiStyle.current].
 */
@Composable
fun BootSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit = {},
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassSurface(
            modifier = modifier,
            cornerRadius = cornerRadius,
            content = content,
        )
        BootUiStyle.MATERIAL3 -> Material3Surface(
            modifier = modifier,
            cornerRadius = cornerRadius,
            content = content,
        )
    }
}
