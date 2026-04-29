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

// ─── Material3Dialog & Snackbar ──────────────────────────────────────────────
@Composable
fun Material3AlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = confirmButton,
        dismissButton = dismissButton
    )
}

@Composable
fun Material3Snackbar(
    message: String,
    modifier: Modifier = Modifier,
    action: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Snackbar(
        modifier = modifier,
        action = if (action != null && onActionClick != null) {
            { TextButton(onClick = onActionClick) { Text(action) } }
        } else null
    ) { Text(message) }
}

@Preview
@Composable
private fun Material3AlertDialogPreview() {
    MaterialTheme {
        Material3AlertDialog(
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
private fun Material3SnackbarPreview() {
    MaterialTheme {
        Material3Snackbar(message = "Message sent", action = "Undo", onActionClick = {})
    }
}
