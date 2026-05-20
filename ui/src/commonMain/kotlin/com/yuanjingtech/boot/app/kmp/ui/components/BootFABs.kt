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
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentFAB
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentSmallFAB
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentLargeFAB
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentExtendedFAB
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassFAB
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassLargeFAB
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassSmallFAB
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassExtendedFAB
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3FloatingActionButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3SmallFloatingActionButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3LargeFloatingActionButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3ExtendedFloatingActionButton

@Composable
fun BootFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String = "Add"
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassFAB(onClick = onClick, modifier = modifier, icon = icon, contentDescription = contentDescription)
        BootUiStyle.MATERIAL3 -> Material3FloatingActionButton(onClick = onClick, modifier = modifier, icon = icon, contentDescription = contentDescription)
        BootUiStyle.FLUENT -> FluentFAB(onClick = onClick, modifier = modifier, icon = icon, contentDescription = contentDescription)
    }
}

@Composable
fun BootSmallFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String = "Add"
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassSmallFAB(onClick = onClick, modifier = modifier, icon = icon, contentDescription = contentDescription)
        BootUiStyle.MATERIAL3 -> Material3SmallFloatingActionButton(onClick = onClick, modifier = modifier, icon = icon, contentDescription = contentDescription)
        BootUiStyle.FLUENT -> FluentSmallFAB(onClick = onClick, modifier = modifier, icon = icon, contentDescription = contentDescription)
    }
}

@Composable
fun BootLargeFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String = "Add"
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassLargeFAB(onClick = onClick, modifier = modifier, icon = icon, contentDescription = contentDescription)
        BootUiStyle.MATERIAL3 -> Material3LargeFloatingActionButton(onClick = onClick, modifier = modifier, icon = icon, contentDescription = contentDescription)
        BootUiStyle.FLUENT -> FluentLargeFAB(onClick = onClick, modifier = modifier, icon = icon, contentDescription = contentDescription)
    }
}

@Composable
fun BootExtendedFloatingActionButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    expanded: Boolean = true
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassExtendedFAB(onClick = onClick, text = text, modifier = modifier, icon = icon)
        BootUiStyle.MATERIAL3 -> Material3ExtendedFloatingActionButton(onClick = onClick, text = text, modifier = modifier, icon = icon, expanded = expanded)
        BootUiStyle.FLUENT -> FluentExtendedFAB(onClick = onClick, text = text, modifier = modifier, icon = icon)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootFABsLiquidGlassPreview() {
    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        BootSmallFloatingActionButton(onClick = {}, icon = Icons.Default.Add)
        BootFloatingActionButton(onClick = {}, icon = Icons.Default.Add)
        BootLargeFloatingActionButton(onClick = {}, icon = Icons.Default.Add)
        BootExtendedFloatingActionButton(onClick = {}, text = "Add", icon = Icons.Default.Add)
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootFABsMaterial3Preview() {
    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        BootSmallFloatingActionButton(onClick = {}, icon = Icons.Default.Add)
        BootFloatingActionButton(onClick = {}, icon = Icons.Default.Add)
        BootLargeFloatingActionButton(onClick = {}, icon = Icons.Default.Add)
        BootExtendedFloatingActionButton(onClick = {}, text = "Add", icon = Icons.Default.Add)
    }
}
