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
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.*
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
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass AssistChip")
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
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass FilterChip")
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
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass SuggestionChip")
        BootUiStyle.MATERIAL3 -> Material3SuggestionChip(
            onClick = onClick,
            label = label,
            modifier = modifier,
            icon = icon,
            enabled = enabled
        )
    }
}

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
