package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LiquidGlassCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = "",
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = if (enabled) 1f else 0.4f
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = { onCheckedChange(!checked) },
            )
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .liquidGlassSurface(cornerRadius = 4.dp, borderAlpha = 0.3f)
                .then(
                    if (checked) Modifier.background(Color.White.copy(alpha = 0.3f)) else Modifier
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = alpha)),
                )
            }
        }
        if (label.isNotEmpty()) {
            Box(modifier = Modifier.padding(start = 8.dp))
            Box(modifier = Modifier) {
                androidx.compose.material3.Text(label, color = Color.White.copy(alpha = alpha))
            }
        }
    }
}

@Composable
fun LiquidGlassRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = "",
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = if (enabled) 1f else 0.4f
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = { onClick?.invoke() },
            )
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .liquidGlassSurface(cornerRadius = 10.dp, borderAlpha = 0.3f),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = alpha)),
                )
            }
        }
        if (label.isNotEmpty()) {
            Box(modifier = Modifier.padding(start = 8.dp))
            Box(modifier = Modifier) {
                androidx.compose.material3.Text(label, color = Color.White.copy(alpha = alpha))
            }
        }
    }
}

@Composable
fun LiquidGlassSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = "",
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = if (enabled) 1f else 0.4f
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = { onCheckedChange(!checked) },
            )
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier
                .height(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .liquidGlassSurface(cornerRadius = 12.dp, borderAlpha = 0.3f),
            contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = alpha)),
            )
        }
        if (label.isNotEmpty()) {
            Box(modifier = Modifier.padding(start = 8.dp))
            Box(modifier = Modifier) {
                androidx.compose.material3.Text(label, color = Color.White.copy(alpha = alpha))
            }
        }
    }
}

@Composable
fun LiquidGlassSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = if (enabled) 1f else 0.4f
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .liquidGlassSurface(cornerRadius = 20.dp, borderAlpha = 0.2f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {},
            )
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        val normalized = (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)
        Box(
            modifier = Modifier
                .fillMaxWidth(normalized.coerceIn(0f, 1f))
                .height(4.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = alpha * 0.6f)),
        )
    }
}

@Preview
@Composable
private fun LiquidGlassSelectionPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            var c by remember { mutableStateOf(false) }
            LiquidGlassCheckbox(checked = c, onCheckedChange = { c = it }, label = "Checkbox")
            var r by remember { mutableStateOf(false) }
            LiquidGlassRadioButton(selected = r, onClick = { r = !r }, label = "Radio Button")
            var s by remember { mutableStateOf(false) }
            LiquidGlassSwitch(checked = s, onCheckedChange = { s = it }, label = "Switch")
            LiquidGlassSlider(value = 0.5f, onValueChange = {})
        }
    }
}
