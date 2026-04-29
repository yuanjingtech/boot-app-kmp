package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .liquidGlassSurface(cornerRadius = 16.dp, borderAlpha = 0.22f)
            .padding(16.dp),
    ) {
        Column(content = content)
    }
}

@Preview
@Composable
private fun LiquidGlassCardPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LiquidGlassCard {
                Text("Card Content")
                Text("More content here")
            }
        }
    }
}
