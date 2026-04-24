package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassCard
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Card

/**
 * Boot-styled card. Routes to [LiquidGlassCard] or [Material3Card]
 * based on [LocalUiStyle.current].
 */
@Composable
fun BootCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassCard(
            modifier = modifier,
            content = content,
        )
        BootUiStyle.MATERIAL3 -> Material3Card(
            modifier = modifier,
            content = content,
        )
    }
}
