package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentButton
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassButton
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Button
import com.yuanjingtech.boot.app.kmp.ui.preview.LiquidGlassPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.Material3PreviewWrapper

@Composable
fun BootButton(onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, content: @Composable RowScope.() -> Unit = {}) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
        BootUiStyle.MATERIAL3 -> Material3Button(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
        BootUiStyle.FLUENT -> FluentButton(onClick = onClick, modifier = modifier, enabled = enabled, content = content)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootButtonLiquidGlassPreview() {
    Column {
        BootButton(onClick = {}) { Text("LiquidGlass Button") }
        BootButton(onClick = {}, enabled = false) { Text("Disabled Button") }
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootButtonMaterial3Preview() {
    Column {
        BootButton(onClick = {}) { Text("Material3 Button") }
        BootButton(onClick = {}, enabled = false) { Text("Disabled Button") }
    }
}

