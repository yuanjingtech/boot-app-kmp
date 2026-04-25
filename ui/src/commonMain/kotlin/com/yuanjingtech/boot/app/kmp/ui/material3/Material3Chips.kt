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

// ─── Material3Chip ───────────────────────────────────────────────────────────
@Composable
fun Material3AssistChip(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label) },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = leadingIcon?.let { { Icon(it, null) } }
    )
}

@Composable
fun Material3FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = if (selected && leadingIcon == null) {
            { Icon(Icons.Default.Check, null, Modifier.size(FilterChipDefaults.IconSize)) }
        } else {
            leadingIcon?.let { { Icon(it, null, Modifier.size(FilterChipDefaults.IconSize)) } }
        }
    )
}

@Composable
fun Material3SuggestionChip(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    SuggestionChip(
        onClick = onClick,
        label = { Text(label) },
        modifier = modifier,
        enabled = enabled,
        icon = icon?.let { { Icon(it, null) } }
    )
}

@Preview
@Composable
private fun Material3ChipPreview() {
    MaterialTheme {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Material3AssistChip(onClick = {}, label = "Assist", leadingIcon = Icons.Default.Add)
                Material3SuggestionChip(onClick = {}, label = "Suggestion", icon = Icons.Default.Star)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                var s1 by remember { mutableStateOf(false) }
                Material3FilterChip(selected = s1, onClick = { s1 = !s1 }, label = "Filter")
                var s2 by remember { mutableStateOf(true) }
                Material3FilterChip(selected = s2, onClick = { s2 = !s2 }, label = "Selected", leadingIcon = Icons.Default.Check)
            }
        }
    }
}
