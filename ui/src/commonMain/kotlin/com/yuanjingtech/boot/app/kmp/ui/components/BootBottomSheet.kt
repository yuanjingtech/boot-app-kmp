package com.yuanjingtech.boot.app.kmp.ui.components
import com.yuanjingtech.boot.app.kmp.ui.preview.*

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentModalBottomSheet
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassModalBottomSheet
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3ModalBottomSheet

@Composable
fun BootModalBottomSheet(onDismiss: () -> Unit, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit = {}) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassModalBottomSheet(modifier = modifier, content = content)
        BootUiStyle.MATERIAL3 -> Material3ModalBottomSheet(onDismiss = onDismiss, modifier = modifier, content = content)
        BootUiStyle.FLUENT -> FluentModalBottomSheet(onDismiss = onDismiss, modifier = modifier, content = content)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootBottomSheetLiquidGlassPreview() {
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

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootBottomSheetMaterial3Preview() {
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
