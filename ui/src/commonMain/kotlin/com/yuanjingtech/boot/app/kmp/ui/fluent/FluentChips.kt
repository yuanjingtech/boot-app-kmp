package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FluentAssistChip(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = if (enabled) colors.controlFillTertiary else colors.controlFillDisabled
    val textColor = if (enabled) colors.textPrimary else colors.textDisabled
    val shape = RoundedCornerShape(4.dp)

    Box(
        modifier = modifier
            .background(backgroundColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leadingIcon?.invoke()
            Text(
                text = label,
                color = textColor,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
fun FluentSuggestionChip(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = if (enabled) colors.controlFillSecondary else colors.controlFillDisabled
    val textColor = if (enabled) colors.textPrimary else colors.textDisabled
    val shape = RoundedCornerShape(4.dp)

    Box(
        modifier = modifier
            .background(backgroundColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.invoke()
            Text(
                text = label,
                color = textColor,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = when {
        !enabled -> colors.controlFillDisabled
        selected -> colors.accent
        else -> colors.controlFillTertiary
    }
    val textColor = when {
        !enabled -> colors.textDisabled
        selected -> colors.accentText
        else -> colors.textPrimary
    }
    val shape = RoundedCornerShape(4.dp)

    Box(
        modifier = modifier
            .background(backgroundColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leadingIcon?.invoke()
            Text(
                text = label,
                color = textColor,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}