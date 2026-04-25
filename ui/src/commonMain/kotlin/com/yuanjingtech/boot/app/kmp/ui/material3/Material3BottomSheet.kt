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

// ─── Material3BottomSheet ────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Material3ModalBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val state = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Material3BottomSheetPreview() {
    MaterialTheme {
        var show by remember { mutableStateOf(false) }
        Column(Modifier.padding(16.dp)) {
            TextButton(onClick = { show = true }) { Text("Show Bottom Sheet") }
            if (show) {
                Material3ModalBottomSheet(onDismiss = { show = false }) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Bottom Sheet Content")
                        Material3ListItem(headlineContent = "Item 1", leadingContent = Icons.Default.Star)
                        Material3ListItem(headlineContent = "Item 2", leadingContent = Icons.Default.Favorite)
                    }
                }
            }
        }
    }
}
