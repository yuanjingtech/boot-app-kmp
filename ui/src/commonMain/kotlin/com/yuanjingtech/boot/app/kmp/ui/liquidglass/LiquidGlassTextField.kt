package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    val textColor = Color.White.copy(alpha = if (enabled) 0.9f else 0.5f)
    val placeholderColor = Color.White.copy(alpha = 0.5f)

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
            cursorBrush = SolidColor(Color.White),
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
