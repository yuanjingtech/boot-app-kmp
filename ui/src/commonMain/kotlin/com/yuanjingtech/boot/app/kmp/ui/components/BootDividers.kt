package com.yuanjingtech.boot.app.kmp.ui.components
import com.yuanjingtech.boot.app.kmp.ui.preview.*

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentHorizontalDivider
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentListItem
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentVerticalDivider
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassHorizontalDivider
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassListItem
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassVerticalDivider
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3HorizontalDivider
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3ListItem
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3VerticalDivider

@Composable
fun BootHorizontalDivider(modifier: Modifier = Modifier, thickness: Dp = 1.dp) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassHorizontalDivider(modifier = modifier, thickness = thickness)
        BootUiStyle.MATERIAL3 -> Material3HorizontalDivider(modifier = modifier, thickness = thickness)
        BootUiStyle.FLUENT -> FluentHorizontalDivider(modifier = modifier, thickness = thickness)
    }
}

@Composable
fun BootVerticalDivider(modifier: Modifier = Modifier, thickness: Dp = 1.dp) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassVerticalDivider(modifier = modifier, thickness = thickness)
        BootUiStyle.MATERIAL3 -> Material3VerticalDivider(modifier = modifier, thickness = thickness)
        BootUiStyle.FLUENT -> FluentVerticalDivider(modifier = modifier, thickness = thickness)
    }
}

@Composable
fun BootListItem(
    headlineContent: String,
    modifier: Modifier = Modifier,
    overlineContent: String = "",
    supportingContent: String = "",
    leadingContent: ImageVector? = null,
    trailingContent: String = ""
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassListItem(headlineContent = headlineContent, modifier = modifier, overlineContent = overlineContent, supportingContent = supportingContent, leadingContent = leadingContent, trailingContent = trailingContent)
        BootUiStyle.MATERIAL3 -> Material3ListItem(headlineContent = headlineContent, modifier = modifier, overlineContent = overlineContent, supportingContent = supportingContent, leadingContent = leadingContent, trailingContent = trailingContent)
        BootUiStyle.FLUENT -> FluentListItem(headlineContent = headlineContent, modifier = modifier, overlineContent = overlineContent, supportingContent = supportingContent, leadingContent = leadingContent, trailingContent = trailingContent)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootDividersLiquidGlassPreview() {
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Above")
        BootHorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Text("Below")
        Row(modifier = Modifier.height(80.dp)) {
            Text("Left")
            BootVerticalDivider(modifier = Modifier.padding(horizontal = 8.dp))
            Text("Right")
        }
        BootListItem(headlineContent = "Headline", overlineContent = "OVERLINE", supportingContent = "Supporting", leadingContent = Icons.Default.Person, trailingContent = "Trailing")
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootDividersMaterial3Preview() {
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Above")
        BootHorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Text("Below")
        Row(modifier = Modifier.height(80.dp)) {
            Text("Left")
            BootVerticalDivider(modifier = Modifier.padding(horizontal = 8.dp))
            Text("Right")
        }
        BootListItem(headlineContent = "Headline", overlineContent = "OVERLINE", supportingContent = "Supporting", leadingContent = Icons.Default.Person, trailingContent = "Trailing")
    }
}
