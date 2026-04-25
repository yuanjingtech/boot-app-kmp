package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassBadge
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassBadgedBox
import com.yuanjingtech.boot.app.kmp.ui.material3.*

@Composable
fun BootBadge(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    content: @Composable RowScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassBadge(
            modifier = modifier,
            backgroundColor = containerColor,
            contentColor = contentColor,
            content = content
        )
        BootUiStyle.MATERIAL3 -> Material3Badge(
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor,
            content = content
        )
    }
}

@Composable
fun BootBadgedBox(
    badgeContent: @Composable BoxScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassBadgedBox(
            badgeContent = badgeContent,
            modifier = modifier,
            content = content
        )
        BootUiStyle.MATERIAL3 -> Material3BadgedBox(
            badgeContent = badgeContent,
            modifier = modifier,
            content = content
        )
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@Preview(name = "LiquidGlass", group = "UI Style")
@Preview(name = "Material3", group = "UI Style")
annotation class BootBadgesStylePreviews

@BootBadgesStylePreviews
@Composable
private fun BootBadgesStylePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            BootBadgedBox(
                badgeContent = { BootBadge { Text("3") } }
            ) {
                Icon(Icons.Default.Notifications, "Notifications")
            }
        }
    }
}

// ─── Legacy single-style preview ──────────────────────────────────────────────

@Preview
@Composable
private fun BootBadgePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            BootBadgedBox(
                badgeContent = { BootBadge { Text("3") } }
            ) {
                Icon(Icons.Default.Notifications, "Notifications")
            }
        }
    }
}
