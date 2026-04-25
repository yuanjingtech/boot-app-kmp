package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LiquidGlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .liquidGlassSurface(cornerRadius = cornerRadius, borderAlpha = 0.18f),
        contentAlignment = Alignment.TopStart,
    ) {
        content()
    }
}
