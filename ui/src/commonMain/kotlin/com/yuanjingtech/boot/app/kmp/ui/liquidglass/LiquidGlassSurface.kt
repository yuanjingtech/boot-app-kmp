package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

@Preview
@Composable
private fun LiquidGlassSurfacePreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LiquidGlassSurface {
                Text("Surface Content")
            }
        }
    }
}
