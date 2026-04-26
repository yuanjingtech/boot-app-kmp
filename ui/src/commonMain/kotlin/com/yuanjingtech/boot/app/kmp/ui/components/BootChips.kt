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
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassAssistChip
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassFilterChip
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassSuggestionChip
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3AssistChip
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3FilterChip
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3SuggestionChip

@Composable
fun BootAssistChip(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassAssistChip(onClick = onClick, label = label, modifier = modifier, leadingIcon = leadingIcon, enabled = enabled)
        BootUiStyle.MATERIAL3 -> Material3AssistChip(onClick = onClick, label = label, modifier = modifier, leadingIcon = leadingIcon, enabled = enabled)
    }
}

@Composable
fun BootFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassFilterChip(selected = selected, onClick = onClick, label = label, modifier = modifier, leadingIcon = leadingIcon, enabled = enabled)
        BootUiStyle.MATERIAL3 -> Material3FilterChip(selected = selected, onClick = onClick, label = label, modifier = modifier, leadingIcon = leadingIcon, enabled = enabled)
    }
}

@Composable
fun BootSuggestionChip(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassSuggestionChip(onClick = onClick, label = label, modifier = modifier, icon = icon, enabled = enabled)
        BootUiStyle.MATERIAL3 -> Material3SuggestionChip(onClick = onClick, label = label, modifier = modifier, icon = icon, enabled = enabled)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootChipsLiquidGlassPreview() {
    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BootAssistChip(onClick = {}, label = "Assist", leadingIcon = Icons.Default.Add)
            BootSuggestionChip(onClick = {}, label = "Suggestion", icon = Icons.Default.Star)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BootFilterChip(selected = false, onClick = {}, label = "Filter")
            BootFilterChip(selected = true, onClick = {}, label = "Selected", leadingIcon = Icons.Default.Check)
        }
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootChipsMaterial3Preview() {
    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BootAssistChip(onClick = {}, label = "Assist", leadingIcon = Icons.Default.Add)
            BootSuggestionChip(onClick = {}, label = "Suggestion", icon = Icons.Default.Star)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BootFilterChip(selected = false, onClick = {}, label = "Filter")
            BootFilterChip(selected = true, onClick = {}, label = "Selected", leadingIcon = Icons.Default.Check)
        }
    }
}
