package com.yuanjingtech.boot.app.kmp.ui.material3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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

// ─── Material3TextField ──────────────────────────────────────────────────────
@Composable
fun Material3TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "",
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val vt = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        singleLine = true,
        visualTransformation = vt,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
    )
}

@Preview
@Composable
private fun Material3TextFieldEmptyPreview() {
    MaterialTheme {
        Column(Modifier.padding(8.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            var text by remember { mutableStateOf("") }
            Material3TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = "Enter text...",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun Material3TextFieldFilledPreview() {
    MaterialTheme {
        Column(Modifier.padding(8.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Material3TextField(
                value = "Sample Text",
                onValueChange = {},
                placeholder = "Enter text...",
                modifier = Modifier.fillMaxWidth()
            )
            Material3TextField(
                value = "password",
                onValueChange = {},
                placeholder = "Password",
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
