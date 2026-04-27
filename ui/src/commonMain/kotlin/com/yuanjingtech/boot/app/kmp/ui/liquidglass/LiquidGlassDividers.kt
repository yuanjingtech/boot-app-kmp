package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.LgEffectConfig
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.effects.rememberLgEffects

@Composable
fun LiquidGlassHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
) {
    val colors = LocalLiquidGlassColors.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(colors.surfaceBorder.copy(alpha = colors.surfaceBorderAlpha * 0.6f)),
    )
}

@Composable
fun LiquidGlassVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
) {
    val colors = LocalLiquidGlassColors.current
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(thickness)
            .background(colors.surfaceBorder.copy(alpha = colors.surfaceBorderAlpha * 0.6f)),
    )
}

@Composable
fun LiquidGlassListItem(
    headlineContent: String,
    modifier: Modifier = Modifier,
    overlineContent: String = "",
    supportingContent: String = "",
    leadingContent: ImageVector? = null,
    trailingContent: String = "",
    config: LgEffectConfig = rememberLgEffects(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .liquidGlassBackdropOrSurface(
                backdrop = null,
                cornerRadius = 12.dp,
                borderAlpha = 0.15f,
                config = config,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingContent != null) {
            Icon(
                imageVector = leadingContent,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(end = 16.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            if (overlineContent.isNotEmpty()) {
                Text(
                    text = overlineContent,
                    color = Color.White.copy(alpha = 0.5f),
                )
            }
            Text(
                text = headlineContent,
                color = Color.White,
            )
            if (supportingContent.isNotEmpty()) {
                Text(
                    text = supportingContent,
                    color = Color.White.copy(alpha = 0.6f),
                )
            }
        }
        if (trailingContent.isNotEmpty()) {
            Text(
                text = trailingContent,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun LiquidGlassDividerPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Above divider", color = Color.White)
            LiquidGlassHorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text("Below divider", color = Color.White)
        }
    }
}

@Preview
@Composable
private fun LiquidGlassListItemPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(8.dp)) {
            LiquidGlassListItem(
                headlineContent = "Headline Text",
                overlineContent = "OVERLINE",
                supportingContent = "Supporting text",
                leadingContent = Icons.Filled.Search,
                trailingContent = "Trailing",
            )
            LiquidGlassHorizontalDivider()
            LiquidGlassListItem(headlineContent = "Simple Item", leadingContent = Icons.Filled.Search)
        }
    }
}
