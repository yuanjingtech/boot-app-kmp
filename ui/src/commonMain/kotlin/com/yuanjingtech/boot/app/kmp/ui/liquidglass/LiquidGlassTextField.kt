package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LiquidGlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "",
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val visualTransformation = when {
        isPassword -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }
    val colors = LocalLiquidGlassColors.current
    val textColor = colors.content.copy(alpha = if (enabled) colors.contentAlpha else colors.disabledAlpha)
    val placeholderColor = colors.secondary.copy(alpha = colors.secondaryAlpha)

    Box(
        modifier = modifier
            .liquidGlassSurface(cornerRadius = 12.dp, borderAlpha = 0.20f)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = TextStyle(color = textColor),
            cursorBrush = SolidColor(colors.content),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(color = placeholderColor),
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}

@Preview
@Composable
private fun LiquidGlassTextFieldPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            var text by remember { mutableStateOf("") }
            LiquidGlassTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = "Enter text...",
            )
        }
    }
}
