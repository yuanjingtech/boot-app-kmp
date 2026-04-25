package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.text.input.KeyboardType
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3TextField

/**
 * Boot-styled text field. Routes to [Material3TextField] based on [LocalUiStyle.current].
 * Note: LiquidGlass TextField is a TODO.
 */
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
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass TextField")
        BootUiStyle.MATERIAL3 -> Material3TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            placeholder = placeholder,
            isPassword = isPassword,
            keyboardType = keyboardType
        )
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@Preview(name = "Empty", group = "Content")
@Preview(name = "Filled", group = "Content")
annotation class BootTextFieldContentPreviews

@Preview(name = "Text input", group = "Type")
@Preview(name = "Password", group = "Type")
annotation class BootTextFieldTypePreviews

@BootTextFieldContentPreviews
@Composable
private fun BootTextFieldContentPreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var t by remember { mutableStateOf("") }
                BootTextField(
                    value = t,
                    onValueChange = { t = it },
                    placeholder = "Enter text...",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@BootTextFieldTypePreviews
@Composable
private fun BootTextFieldTypePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BootTextField(
                    value = "Sample Text",
                    onValueChange = {},
                    placeholder = "Enter text...",
                    modifier = Modifier.fillMaxWidth()
                )
                BootTextField(
                    value = "password",
                    onValueChange = {},
                    placeholder = "Password",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ─── Legacy single-style preview ───────────────────────────────────────────────

@Preview
@Composable
private fun BootTextFieldPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var t by remember { mutableStateOf("") }
                BootTextField(
                    value = t,
                    onValueChange = { t = it },
                    placeholder = "Enter text...",
                    modifier = Modifier.fillMaxWidth()
                )
                BootTextField(
                    value = "password",
                    onValueChange = {},
                    placeholder = "Password",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
