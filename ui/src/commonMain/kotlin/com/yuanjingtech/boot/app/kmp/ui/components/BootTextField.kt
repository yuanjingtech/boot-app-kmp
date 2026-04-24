package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassTextField
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3TextField

/**
 * Boot-styled text field. Routes to [LiquidGlassTextField] or [Material3TextField]
 * based on [LocalUiStyle.current].
 */
@Composable
fun BootTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "",
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            placeholder = placeholder,
            isPassword = isPassword,
            keyboardType = keyboardType,
        )
        BootUiStyle.MATERIAL3 -> Material3TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            placeholder = placeholder,
            isPassword = isPassword,
            keyboardType = keyboardType,
        )
    }
}
