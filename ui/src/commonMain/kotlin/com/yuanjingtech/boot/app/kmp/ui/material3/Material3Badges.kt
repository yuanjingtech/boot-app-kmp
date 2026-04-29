package com.yuanjingtech.boot.app.kmp.ui.material3

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

// ─── Material3Badge ──────────────────────────────────────────────────────────
@Composable
fun Material3Badge(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.error,
    contentColor: Color = MaterialTheme.colorScheme.onError,
    content: @Composable RowScope.() -> Unit
) {
    Badge(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content
    )
}

@Composable
fun Material3BadgedBox(
    badgeContent: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    BadgedBox(badge = badgeContent, modifier = modifier, content = content)
}

@Preview
@Composable
private fun Material3BadgePreview() {
    MaterialTheme {
        Column(Modifier.padding(8.dp)) {
            Material3BadgedBox(badgeContent = { Material3Badge { Text("3") } }) {
                Icon(Icons.Default.Notifications, "Notifications")
            }
        }
    }
}
