package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassBadge
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassBadgedBox
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Badge
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3BadgedBox
import com.yuanjingtech.boot.app.kmp.ui.preview.LiquidGlassPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.Material3PreviewWrapper

@Composable
fun BootBadge(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    content: @Composable RowScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassBadge(modifier = modifier, backgroundColor = containerColor, contentColor = contentColor, content = content)
        BootUiStyle.MATERIAL3 -> Material3Badge(modifier = modifier, containerColor = containerColor, contentColor = contentColor, content = content)
    }
}

@Composable
fun BootBadgedBox(
    badgeContent: @Composable BoxScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassBadgedBox(badgeContent = badgeContent, modifier = modifier, content = content)
        BootUiStyle.MATERIAL3 -> Material3BadgedBox(badgeContent = badgeContent, modifier = modifier, content = content)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootBadgesLiquidGlassPreview() {
    BootBadgedBox(badgeContent = { BootBadge { Text("3") } }) {
        Icon(Icons.Default.Notifications, "Notifications")
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootBadgesMaterial3Preview() {
    BootBadgedBox(badgeContent = { BootBadge { Text("3") } }) {
        Icon(Icons.Default.Notifications, "Notifications")
    }
}
