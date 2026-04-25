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
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassAssistChip
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassFilterChip
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassSuggestionChip
import com.yuanjingtech.boot.app.kmp.ui.material3.*

@Composable
fun BootAssistChip(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassAssistChip(
            onClick = onClick,
            label = label,
            modifier = modifier,
            leadingIcon = leadingIcon,
            enabled = enabled
        )
        BootUiStyle.MATERIAL3 -> Material3AssistChip(
            onClick = onClick,
            label = label,
            modifier = modifier,
            leadingIcon = leadingIcon,
            enabled = enabled
        )
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
        BootUiStyle.LIQUID_GLASS -> LiquidGlassFilterChip(
            selected = selected,
            onClick = onClick,
            label = label,
            modifier = modifier,
            leadingIcon = leadingIcon,
            enabled = enabled
        )
        BootUiStyle.MATERIAL3 -> Material3FilterChip(
            selected = selected,
            onClick = onClick,
            label = label,
            modifier = modifier,
            leadingIcon = leadingIcon,
            enabled = enabled
        )
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
        BootUiStyle.LIQUID_GLASS -> LiquidGlassSuggestionChip(
            onClick = onClick,
            label = label,
            modifier = modifier,
            icon = icon,
            enabled = enabled
        )
        BootUiStyle.MATERIAL3 -> Material3SuggestionChip(
            onClick = onClick,
            label = label,
            modifier = modifier,
            icon = icon,
            enabled = enabled
        )
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@Preview(name = "LiquidGlass", group = "UI Style")
@Preview(name = "Material3", group = "UI Style")
annotation class BootChipsStylePreviews

@Preview(name = "Assist", group = "Type")
@Preview(name = "Filter", group = "Type")
@Preview(name = "Suggestion", group = "Type")
annotation class BootChipsTypePreviews

@Preview(name = "Unselected", group = "State")
@Preview(name = "Selected", group = "State")
annotation class BootChipsSelectedPreviews

@BootChipsStylePreviews
@Composable
private fun BootChipsStylePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
    }
}

@BootChipsTypePreviews
@Composable
private fun BootChipsTypePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BootAssistChip(onClick = {}, label = "Assist", leadingIcon = Icons.Default.Add)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BootFilterChip(selected = true, onClick = {}, label = "Filter")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BootSuggestionChip(onClick = {}, label = "Suggestion", icon = Icons.Default.Star)
                }
            }
        }
    }
}

@BootChipsSelectedPreviews
@Composable
private fun BootChipsSelectedPreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BootFilterChip(selected = false, onClick = {}, label = "Unselected")
                    BootFilterChip(selected = true, onClick = {}, label = "Selected", leadingIcon = Icons.Default.Check)
                }
            }
        }
    }
}

// ─── Legacy single-style preview ──────────────────────────────────────────────

@Preview
@Composable
private fun BootChipPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BootAssistChip(
                        onClick = {},
                        label = "Assist",
                        leadingIcon = Icons.Default.Add
                    )
                    BootSuggestionChip(
                        onClick = {},
                        label = "Suggestion",
                        icon = Icons.Default.Star
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    var s1 by remember { mutableStateOf(false) }
                    BootFilterChip(
                        selected = s1,
                        onClick = { s1 = !s1 },
                        label = "Filter"
                    )
                    var s2 by remember { mutableStateOf(true) }
                    BootFilterChip(
                        selected = s2,
                        onClick = { s2 = !s2 },
                        label = "Selected",
                        leadingIcon = Icons.Default.Check
                    )
                }
            }
        }
    }
}
