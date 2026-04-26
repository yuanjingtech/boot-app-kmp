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
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassAlertDialog
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassSnackbar
import com.yuanjingtech.boot.app.kmp.ui.material3.*

@Composable
fun BootAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassAlertDialog(onDismissRequest = onDismissRequest, title = title, text = text, confirmButton = confirmButton, dismissButton = dismissButton)
        BootUiStyle.MATERIAL3 -> Material3AlertDialog(onDismissRequest = onDismissRequest, title = title, text = text, confirmButton = confirmButton, dismissButton = dismissButton)
    }
}

@Composable
fun BootSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    action: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassSnackbar(message = message, modifier = modifier, action = action, onActionClick = onActionClick)
        BootUiStyle.MATERIAL3 -> Material3Snackbar(message = message, modifier = modifier, action = action, onActionClick = onActionClick)
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@StylePreviews
@Composable
private fun BootDialogsStylePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides LocalUiStyle.current) {
            Column(modifier = Modifier.padding(8.dp)) {
                BootSnackbar(message = "Message sent", action = "Undo", onActionClick = {})
                BootAlertDialog(
                    onDismissRequest = {},
                    title = "Dialog Title",
                    text = "This is a dialog message.",
                    confirmButton = { TextButton(onClick = {}) { Text("Confirm") } },
                    dismissButton = { TextButton(onClick = {}) { Text("Cancel") } }
                )
            }
        }
    }
}

@Preview(name = "AlertDialog", group = "Type")
@Preview(name = "Snackbar", group = "Type")
annotation class BootDialogsTypePreviews

@BootDialogsTypePreviews
@Composable
private fun BootDialogsTypePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(modifier = Modifier.padding(8.dp)) {
                BootSnackbar(message = "Message sent", action = "Undo", onActionClick = {})
            }
        }
    }
}

// ─── Legacy single-style preview ──────────────────────────────────────────────

@Preview
@Composable
private fun BootDialogsPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(modifier = Modifier.padding(8.dp)) {
                BootSnackbar(message = "Message sent", action = "Undo", onActionClick = {})
                BootAlertDialog(
                    onDismissRequest = {},
                    title = "Dialog Title",
                    text = "This is a dialog message.",
                    confirmButton = { TextButton(onClick = {}) { Text("Confirm") } },
                    dismissButton = { TextButton(onClick = {}) { Text("Cancel") } }
                )
            }
        }
    }
}
