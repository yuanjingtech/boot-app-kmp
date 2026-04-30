package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.yuanjingtech.boot.app.kmp.ui.components.shimmerEffect

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .liquidGlassSurface(cornerRadius = 16.dp, borderAlpha = 0.22f),
    ) {
        if (isLoading) {
            // Loading skeleton content
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
            ) {
                // Title skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .shimmerEffect(visible = true, cornerRadius = 6.dp),
                )
                // Subtitle skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(16.dp)
                        .shimmerEffect(visible = true, cornerRadius = 6.dp),
                )
                // Body skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .shimmerEffect(visible = true, cornerRadius = 6.dp),
                )
            }
        } else {
            Column(modifier = Modifier.padding(16.dp), content = content)
        }
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
