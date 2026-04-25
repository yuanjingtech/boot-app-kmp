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
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.*
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
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass AlertDialog")
        BootUiStyle.MATERIAL3 -> Material3AlertDialog(
            onDismissRequest = onDismissRequest,
            title = title,
            text = text,
            confirmButton = confirmButton,
            dismissButton = dismissButton
        )
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
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass Snackbar")
        BootUiStyle.MATERIAL3 -> Material3Snackbar(
            message = message,
            modifier = modifier,
            action = action,
            onActionClick = onActionClick
        )
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@Preview(name = "LiquidGlass", group = "UI Style")
@Preview(name = "Material3", group = "UI Style")
annotation class BootDialogsStylePreviews

@Preview(name = "AlertDialog", group = "Type")
@Preview(name = "Snackbar", group = "Type")
annotation class BootDialogsTypePreviews

@BootDialogsStylePreviews
@Composable
private fun BootDialogsStylePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(modifier = Modifier.padding(8.dp)) {
                BootSnackbar(
                    message = "Message sent",
                    action = "Undo",
                    onActionClick = {}
                )
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

@BootDialogsTypePreviews
@Composable
private fun BootDialogsTypePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(modifier = Modifier.padding(8.dp)) {
                BootSnackbar(
                    message = "Message sent",
                    action = "Undo",
                    onActionClick = {}
                )
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
                BootSnackbar(
                    message = "Message sent",
                    action = "Undo",
                    onActionClick = {}
                )
                BootAlertDialog(
                    onDismissRequest = {},
                    title = "Dialog Title",
                    text = "This is a dialog message.",
                    confirmButton = {
                        TextButton(onClick = {}) { Text("Confirm") }
                    },
                    dismissButton = {
                        TextButton(onClick = {}) { Text("Cancel") }
                    }
                )
            }
        }
    }
}
