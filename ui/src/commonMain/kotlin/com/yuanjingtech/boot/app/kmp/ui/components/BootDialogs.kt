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
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentAlertDialog
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentSnackbar
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassAlertDialog
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassSnackbar
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3AlertDialog
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Snackbar

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
        BootUiStyle.FLUENT -> FluentAlertDialog(onDismissRequest = onDismissRequest, title = title, text = text, confirmButton = confirmButton, dismissButton = dismissButton)
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
        BootUiStyle.FLUENT -> FluentSnackbar(message = message, modifier = modifier, action = action, onActionClick = onActionClick)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootDialogsLiquidGlassPreview() {
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

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootDialogsMaterial3Preview() {
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