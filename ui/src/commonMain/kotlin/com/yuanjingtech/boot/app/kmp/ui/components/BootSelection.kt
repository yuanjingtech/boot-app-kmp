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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassCheckbox
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassRadioButton
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassSwitch
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassSlider
import com.yuanjingtech.boot.app.kmp.ui.material3.*

@Composable
fun BootCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, label: String = "") {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassCheckbox(checked = checked, onCheckedChange = onCheckedChange, modifier = modifier, enabled = enabled, label = label)
        BootUiStyle.MATERIAL3 -> Material3Checkbox(checked = checked, onCheckedChange = onCheckedChange, modifier = modifier, enabled = enabled, label = label)
    }
}

@Composable
fun BootRadioButton(selected: Boolean, onClick: (() -> Unit)? = null, modifier: Modifier = Modifier, enabled: Boolean = true, label: String = "") {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassRadioButton(selected = selected, onClick = onClick, modifier = modifier, enabled = enabled, label = label)
        BootUiStyle.MATERIAL3 -> Material3RadioButton(selected = selected, onClick = onClick, modifier = modifier, enabled = enabled, label = label)
    }
}

@Composable
fun BootSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, label: String = "") {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassSwitch(checked = checked, onCheckedChange = onCheckedChange, modifier = modifier, enabled = enabled, label = label)
        BootUiStyle.MATERIAL3 -> Material3Switch(checked = checked, onCheckedChange = onCheckedChange, modifier = modifier, enabled = enabled, label = label)
    }
}

@Composable
fun BootSlider(value: Float, onValueChange: (Float) -> Unit, modifier: Modifier = Modifier, valueRange: ClosedFloatingPointRange<Float> = 0f..1f, steps: Int = 0, enabled: Boolean = true) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassSlider(value = value, onValueChange = onValueChange, modifier = modifier, valueRange = valueRange, steps = steps, enabled = enabled)
        BootUiStyle.MATERIAL3 -> Material3Slider(value = value, onValueChange = onValueChange, modifier = modifier, valueRange = valueRange, steps = steps, enabled = enabled)
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@StylePreviews
@Composable
private fun BootSelectionStylePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides LocalUiStyle.current) {
            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                var c by remember { mutableStateOf(false) }
                BootCheckbox(checked = c, onCheckedChange = { c = it }, label = "Checkbox")
                var r by remember { mutableStateOf(false) }
                BootRadioButton(selected = r, onClick = { r = !r }, label = "Radio")
                var s by remember { mutableStateOf(false) }
                BootSwitch(checked = s, onCheckedChange = { s = it }, label = "Switch")
                BootSlider(value = 0.5f, onValueChange = {})
            }
        }
    }
}

@Preview(name = "Checkbox", group = "Type")
@Preview(name = "RadioButton", group = "Type")
@Preview(name = "Switch", group = "Type")
@Preview(name = "Slider", group = "Type")
annotation class BootSelectionTypePreviews

@BootSelectionTypePreviews
@Composable
private fun BootSelectionTypePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                var c by remember { mutableStateOf(false) }
                BootCheckbox(checked = c, onCheckedChange = { c = it }, label = "Checkbox")
                var r by remember { mutableStateOf(false) }
                BootRadioButton(selected = r, onClick = { r = !r }, label = "Radio")
                var s by remember { mutableStateOf(false) }
                BootSwitch(checked = s, onCheckedChange = { s = it }, label = "Switch")
                BootSlider(value = 0.5f, onValueChange = {})
            }
        }
    }
}

@Preview(name = "Enabled", group = "State")
@Preview(name = "Disabled", group = "State")
annotation class BootSelectionStatePreviews

@BootSelectionStatePreviews
@Composable
private fun BootSelectionStatePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                var c by remember { mutableStateOf(false) }
                BootCheckbox(checked = c, onCheckedChange = { c = it }, label = "Enabled", enabled = true)
                var d by remember { mutableStateOf(false) }
                BootCheckbox(checked = d, onCheckedChange = { d = it }, label = "Disabled", enabled = false)
            }
        }
    }
}

// ─── Legacy single-style preview ──────────────────────────────────────────────

@Preview
@Composable
private fun BootSelectionPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                var c by remember { mutableStateOf(false) }
                BootCheckbox(checked = c, onCheckedChange = { c = it }, label = "Checkbox")
                var r by remember { mutableStateOf(false) }
                BootRadioButton(selected = r, onClick = { r = !r }, label = "Radio Button")
                var s by remember { mutableStateOf(false) }
                BootSwitch(checked = s, onCheckedChange = { s = it }, label = "Switch")
                BootSlider(value = 0.5f, onValueChange = {})
            }
        }
    }
}
