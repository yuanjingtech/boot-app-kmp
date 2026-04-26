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
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3ElevatedButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3FilledTonalButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3IconButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3OutlinedButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3TextButton

@Composable
fun BootFilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
        BootUiStyle.MATERIAL3 -> Material3FilledTonalButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
    }
}

@Composable
fun BootOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
        BootUiStyle.MATERIAL3 -> Material3OutlinedButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
    }
}

@Composable
fun BootTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
        BootUiStyle.MATERIAL3 -> Material3TextButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
    }
}

@Composable
fun BootElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
        BootUiStyle.MATERIAL3 -> Material3ElevatedButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
    }
}

@Composable
fun BootIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector,
    contentDescription: String
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassButton(
            onClick = onClick, modifier = modifier, enabled = enabled,
            content = { Icon(imageVector = icon, contentDescription = contentDescription, tint = Color.White) }
        )

        BootUiStyle.MATERIAL3 -> Material3IconButton(
            onClick = onClick, modifier = modifier, enabled = enabled,
            icon = icon, contentDescription = contentDescription
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootButtonsLiquidGlassPreview() {
    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BootFilledTonalButton(onClick = {}) { Text("Tonal") }
        BootOutlinedButton(onClick = {}) { Text("Outlined") }
        BootTextButton(onClick = {}) { Text("Text") }
        BootElevatedButton(onClick = {}) { Text("Elevated") }
        BootIconButton(onClick = {}, icon = Icons.Default.Add, contentDescription = "Add")
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootButtonsMaterial3Preview() {
    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BootFilledTonalButton(onClick = {}) { Text("Tonal") }
        BootOutlinedButton(onClick = {}) { Text("Outlined") }
        BootTextButton(onClick = {}) { Text("Text") }
        BootElevatedButton(onClick = {}) { Text("Elevated") }
        BootIconButton(onClick = {}, icon = Icons.Default.Add, contentDescription = "Add")
    }
}