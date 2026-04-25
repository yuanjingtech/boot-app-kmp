package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LiquidGlassBadge(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White.copy(alpha = 0.9f),
    contentColor: Color = Color.Black,
    content: @Composable RowScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .size(16.dp)
            .clip(CircleShape)
            .liquidGlassSurface(cornerRadius = 8.dp, borderAlpha = 0.4f),
        contentAlignment = Alignment.Center,
    ) {
        Row { content() }
    }
}

@Composable
fun LiquidGlassBadgedBox(
    badgeContent: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        content()
        Box(
            modifier = Modifier.align(Alignment.TopEnd),
            content = badgeContent,
        )
    }
}

@Preview
@Composable
private fun LiquidGlassBadgePreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(8.dp)) {
            LiquidGlassBadgedBox(
                badgeContent = {
                    LiquidGlassBadge {
                        Text("3", color = Color.Black)
                    }
                }
            ) {
                Text("Icon")
            }
        }
    }
}
