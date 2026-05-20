package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper

@Composable
fun FluentCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = { onCheckedChange?.invoke(!checked) },
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val boxColor = when {
            !enabled -> colors.controlFillDisabled
            checked -> colors.accent
            else -> colors.controlFill
        }
        val borderColor = when {
            !enabled -> colors.controlStrokeDisabled
            else -> colors.controlStrokeDefault
        }

        Box(
            modifier = Modifier
                .background(boxColor, RoundedCornerShape(2.dp))
                .padding(2.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                FluentText("✓", color = colors.accentText)
            }
        }

        if (label != null) {
            FluentText(
                text = label,
                color = if (enabled) colors.textPrimary else colors.textDisabled,
            )
        }
    }
}

@Composable
fun FluentRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = { onClick?.invoke() },
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val outerColor = when {
            !enabled -> colors.controlStrokeDisabled
            else -> colors.controlStrokeDefault
        }
        val innerColor = if (selected) colors.accent else Color.Transparent

        Box(
            modifier = Modifier
                .background(outerColor, RoundedCornerShape(50))
                .padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .background(innerColor, RoundedCornerShape(50)),
            )
        }

        if (label != null) {
            FluentText(
                text = label,
                color = if (enabled) colors.textPrimary else colors.textDisabled,
            )
        }
    }
}

@Composable
fun FluentSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = { onCheckedChange?.invoke(!checked) },
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val trackColor = when {
            !enabled -> colors.controlFillDisabled
            checked -> colors.accent
            else -> colors.controlFill
        }
        val thumbColor = when {
            !enabled -> colors.textDisabled
            checked -> colors.accentText
            else -> colors.textPrimary
        }

        Box(
            modifier = Modifier
                .background(trackColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 2.dp, vertical = 2.dp),
            contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier
                    .background(thumbColor, RoundedCornerShape(50))
                    .padding(8.dp),
            )
        }

        if (label != null) {
            FluentText(
                text = label,
                color = if (enabled) colors.textPrimary else colors.textDisabled,
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentSelectionControlsPreview() {
    var checked1 by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(0) }
    var checked2 by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(16.dp)) {
        FluentCheckbox(
            checked = checked1,
            onCheckedChange = { checked1 = it },
            label = "Checkbox"
        )
        FluentRadioButton(
            selected = selected == 0,
            onClick = { selected = 0 },
            label = "Radio 1"
        )
        FluentRadioButton(
            selected = selected == 1,
            onClick = { selected = 1 },
            label = "Radio 2"
        )
        FluentSwitch(
            checked = checked2,
            onCheckedChange = { checked2 = it },
            label = "Switch"
        )
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentSelectionControlsLightPreview() {
    var checked by remember { mutableStateOf(true) }
    Column(modifier = Modifier.padding(16.dp)) {
        FluentCheckbox(checked = checked, onCheckedChange = { checked = it }, label = "Enabled")
        FluentSwitch(checked = checked, onCheckedChange = { checked = it }, label = "Light Theme")
    }
}