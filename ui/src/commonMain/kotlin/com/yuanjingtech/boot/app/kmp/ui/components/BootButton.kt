package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Button

/**
 * Boot-styled button. Routes to [LiquidGlassButton] or [Material3Button]
 * based on [LocalUiStyle.current].
 */
@Composable
fun BootButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {},
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            content = content,
        )
        BootUiStyle.MATERIAL3 -> Material3Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            content = content,
        )
    }
}
