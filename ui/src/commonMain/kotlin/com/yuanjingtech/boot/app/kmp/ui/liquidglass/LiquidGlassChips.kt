package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LiquidGlassAssistChip(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = if (enabled) 1f else 0.4f
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .liquidGlassSurface(cornerRadius = 8.dp, borderAlpha = 0.25f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color.White.copy(alpha = alpha),
                modifier = Modifier.size(18.dp),
            )
            Box(modifier = Modifier.padding(end = 6.dp))
        }
        Text(text = label, color = Color.White.copy(alpha = alpha))
    }
}

@Composable
fun LiquidGlassFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = if (enabled) 1f else 0.4f
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (selected) {
                    Modifier.border(
                        width = 1.5.dp,
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                    )
                } else Modifier
            )
            .liquidGlassSurface(cornerRadius = 8.dp, borderAlpha = if (selected) 0.5f else 0.2f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconToShow = if (selected) Icons.Filled.Check else leadingIcon
        if (iconToShow != null) {
            Icon(
                imageVector = iconToShow,
                contentDescription = null,
                tint = Color.White.copy(alpha = alpha),
                modifier = Modifier.size(18.dp),
            )
            Box(modifier = Modifier.padding(end = 6.dp))
        }
        Text(text = label, color = Color.White.copy(alpha = alpha))
    }
}

@Composable
fun LiquidGlassSuggestionChip(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = if (enabled) 1f else 0.4f
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .liquidGlassSurface(cornerRadius = 8.dp, borderAlpha = 0.2f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = alpha),
                modifier = Modifier.size(18.dp),
            )
            Box(modifier = Modifier.padding(end = 6.dp))
        }
        Text(text = label, color = Color.White.copy(alpha = alpha))
    }
}

@Preview
@Composable
private fun LiquidGlassChipPreview() {
    MaterialTheme {
        Row(modifier = Modifier.padding(8.dp)) {
            LiquidGlassAssistChip(onClick = {}, label = "Assist", leadingIcon = Icons.Filled.Check)
            LiquidGlassSuggestionChip(onClick = {}, label = "Suggestion", icon = Icons.Filled.Check)
        }
        Row(modifier = Modifier.padding(8.dp)) {
            var s1 by mutableStateOf(false)
            LiquidGlassFilterChip(selected = s1, onClick = { s1 = !s1 }, label = "Filter")
            var s2 by mutableStateOf(true)
            LiquidGlassFilterChip(selected = s2, onClick = { s2 = !s2 }, label = "Selected")
        }
    }
}
