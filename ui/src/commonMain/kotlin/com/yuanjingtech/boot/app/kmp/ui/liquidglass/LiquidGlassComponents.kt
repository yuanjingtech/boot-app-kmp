package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// ─── LiquidGlassButton ────────────────────────────────────────────────────────

@Composable
fun LiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val alpha = if (enabled) 1f else 0.4f
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .liquidGlassSurface(cornerRadius = 12.dp, borderAlpha = 0.30f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.background(Color.Transparent),
            content = content,
        )
    }
}

// ─── LiquidGlassCard ─────────────────────────────────────────────────────────

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .liquidGlassSurface(cornerRadius = 16.dp, borderAlpha = 0.22f)
            .padding(16.dp),
    ) {
        androidx.compose.foundation.layout.Column(content = content)
    }
}

// ─── LiquidGlassSurface ───────────────────────────────────────────────────────

@Composable
fun LiquidGlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: androidx.compose.ui.unit.Dp = 16.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .liquidGlassSurface(cornerRadius = cornerRadius, borderAlpha = 0.18f),
        contentAlignment = Alignment.TopStart,
    ) {
        content()
    }
}

// ─── LiquidGlassTextField ────────────────────────────────────────────────────

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

private fun Modifier.fillMaxWidth(): Modifier = this.fillMaxWidth()
