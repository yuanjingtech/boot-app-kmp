package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Button

@Composable
fun BootButton(onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, content: @Composable RowScope.() -> Unit = {}) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
        BootUiStyle.MATERIAL3 -> Material3Button(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@BootPreviews
@Composable
private fun BootButtonStyleAndModePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides LocalUiStyle.current) {
            BootButton(onClick = {}) { Text("Boot Button") }
        }
    }
}

// ─── Legacy single-style previews ────────────────────────────────────────────

@Preview
@Composable
private fun BootButtonLiquidGlassPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {
            BootButton(onClick = {}) { Text("LiquidGlass Button") }
        }
    }
}

@Preview
@Composable
private fun BootButtonMaterial3Preview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            BootButton(onClick = {}) { Text("Material3 Button") }
        }
    }
}

@Preview
@Composable
fun BootButtonDisabledPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {
            BootButton(onClick = {}, enabled = false) { Text("Disabled Button") }
        }
    }
}
