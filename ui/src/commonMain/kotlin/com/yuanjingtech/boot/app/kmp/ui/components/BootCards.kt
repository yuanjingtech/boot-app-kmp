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
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentCard
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentElevatedCard
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassCard
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3ElevatedCard
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3OutlinedCard

@Composable
fun BootElevatedCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassCard(modifier = modifier, content = content)
        BootUiStyle.MATERIAL3 -> Material3ElevatedCard(modifier = modifier, content = content)
        BootUiStyle.FLUENT -> FluentElevatedCard(modifier = modifier, content = content)
    }
}

@Composable
fun BootOutlinedCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassCard(modifier = modifier, content = content)
        BootUiStyle.MATERIAL3 -> Material3OutlinedCard(modifier = modifier, content = content)
        BootUiStyle.FLUENT -> FluentCard(modifier = modifier, content = content)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootCardsLiquidGlassPreview() {
    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BootElevatedCard { Text("Elevated Card") }
        BootOutlinedCard { Text("Outlined Card") }
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootCardsMaterial3Preview() {
    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BootElevatedCard { Text("Elevated Card") }
        BootOutlinedCard { Text("Outlined Card") }
    }
}
