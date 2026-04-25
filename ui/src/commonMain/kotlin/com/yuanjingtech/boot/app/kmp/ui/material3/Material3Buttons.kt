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

// ─── Material3Button ─────────────────────────────────────────────────────────
@Composable
fun Material3Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        content = content
    )
}

@Composable
fun Material3FilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content
    )
}

@Composable
fun Material3OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content
    )
}

@Composable
fun Material3TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content
    )
}

@Composable
fun Material3ElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content
    )
}

@Composable
fun Material3IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector,
    contentDescription: String
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.filledIconButtonColors()
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

@Preview
@Composable
private fun Material3ButtonPreview() {
    MaterialTheme {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Material3Button(onClick = {}) { Text("Primary Button") }
            Material3FilledTonalButton(onClick = {}) { Text("Tonal Button") }
            Material3OutlinedButton(onClick = {}) { Text("Outlined Button") }
            Material3TextButton(onClick = {}) { Text("Text Button") }
            Material3ElevatedButton(onClick = {}) { Text("Elevated Button") }
            Material3IconButton(onClick = {}, icon = Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Preview
@Composable
private fun Material3ButtonDisabledPreview() {
    MaterialTheme {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Material3Button(onClick = {}, enabled = false) { Text("Disabled") }
            Material3FilledTonalButton(onClick = {}, enabled = false) { Text("Disabled Tonal") }
        }
    }
}
