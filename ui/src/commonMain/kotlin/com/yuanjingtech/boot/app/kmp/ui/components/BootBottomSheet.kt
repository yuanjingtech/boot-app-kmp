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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.*
import com.yuanjingtech.boot.app.kmp.ui.material3.*

@Composable
fun BootModalBottomSheet(onDismiss: () -> Unit, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit = {}) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassModalBottomSheet(modifier = modifier, content = content)
        BootUiStyle.MATERIAL3 -> Material3ModalBottomSheet(onDismiss = onDismiss, modifier = modifier, content = content)
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@StylePreviews
@Composable
private fun BootBottomSheetStylePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides LocalUiStyle.current) {
            var show by remember { mutableStateOf(false) }
            Column(modifier = Modifier.padding(16.dp)) {
                TextButton(onClick = { show = true }) { Text("Show Bottom Sheet") }
                if (show) {
                    BootModalBottomSheet(onDismiss = { show = false }) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Bottom Sheet Content")
                            BootListItem(headlineContent = "Item 1", leadingContent = Icons.Default.Star)
                            BootListItem(headlineContent = "Item 2", leadingContent = Icons.Default.Favorite)
                        }
                    }
                }
            }
        }
    }
}

// ─── Legacy single-style preview ──────────────────────────────────────────────

@Preview
@Composable
private fun BootBottomSheetPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            var show by remember { mutableStateOf(false) }
            Column(modifier = Modifier.padding(16.dp)) {
                TextButton(onClick = { show = true }) { Text("Show Bottom Sheet") }
                if (show) {
                    BootModalBottomSheet(onDismiss = { show = false }) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Bottom Sheet Content")
                            BootListItem(headlineContent = "Item 1", leadingContent = Icons.Default.Star)
                            BootListItem(headlineContent = "Item 2", leadingContent = Icons.Default.Favorite)
                        }
                    }
                }
            }
        }
    }
}
