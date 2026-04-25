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
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.*
import com.yuanjingtech.boot.app.kmp.ui.material3.*

@Composable
fun BootBadge(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    content: @Composable RowScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass Badge")
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
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass BadgedBox")
        BootUiStyle.MATERIAL3 -> Material3BadgedBox(
            badgeContent = badgeContent,
            modifier = modifier,
            content = content
        )
    }
}

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
