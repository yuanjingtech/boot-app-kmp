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

// ─── Material3Selection ──────────────────────────────────────────────────────
@Composable
fun Material3Checkbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = ""
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
        if (label.isNotEmpty()) Text(label)
    }
}

@Composable
fun Material3RadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = ""
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        RadioButton(selected = selected, onClick = onClick, enabled = enabled)
        if (label.isNotEmpty()) Text(label)
    }
}

@Composable
fun Material3Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = ""
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors()
        )
        if (label.isNotEmpty()) {
            Spacer(Modifier.width(8.dp))
            Text(label)
        }
    }
}

@Composable
fun Material3Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        valueRange = valueRange,
        steps = steps,
        enabled = enabled,
        colors = SliderDefaults.colors()
    )
}

@Preview
@Composable
private fun Material3SelectionPreview() {
    MaterialTheme {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            var c by remember { mutableStateOf(false) }
            Material3Checkbox(checked = c, onCheckedChange = { c = it }, label = "Checkbox")
            var r by remember { mutableStateOf(false) }
            Material3RadioButton(selected = r, onClick = { r = !r }, label = "Radio Button")
            var s by remember { mutableStateOf(false) }
            Material3Switch(checked = s, onCheckedChange = { s = it }, label = "Switch")
            Material3Slider(value = 0.5f, onValueChange = {})
        }
    }
}
