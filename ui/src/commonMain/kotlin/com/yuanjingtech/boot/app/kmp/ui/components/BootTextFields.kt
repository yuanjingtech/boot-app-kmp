package com.yuanjingtech.boot.app.kmp.ui.components
import com.yuanjingtech.boot.app.kmp.ui.preview.*

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentTextField
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassTextField
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3TextField
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun BootTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "",
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassTextField(value = value, onValueChange = onValueChange, modifier = modifier, enabled = enabled, placeholder = placeholder, isPassword = isPassword, keyboardType = keyboardType)
        BootUiStyle.MATERIAL3 -> Material3TextField(value = value, onValueChange = onValueChange, modifier = modifier, enabled = enabled, placeholder = placeholder, isPassword = isPassword, keyboardType = keyboardType)
        BootUiStyle.FLUENT -> FluentTextField(value = value, onValueChange = onValueChange, modifier = modifier, enabled = enabled, placeholder = placeholder, isPassword = isPassword, keyboardType = keyboardType)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootTextFieldLiquidGlassPreview() {
    Column(modifier = Modifier.padding(8.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        var t by remember { mutableStateOf("") }
        BootTextField(value = t, onValueChange = { t = it }, placeholder = "Enter text...", modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootTextFieldMaterial3Preview() {
    Column(modifier = Modifier.padding(8.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        var t by remember { mutableStateOf("") }
        BootTextField(value = t, onValueChange = { t = it }, placeholder = "Enter text...", modifier = Modifier.fillMaxWidth())
    }
}
