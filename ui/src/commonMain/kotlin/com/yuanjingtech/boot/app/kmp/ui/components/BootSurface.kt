package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.Column
import com.yuanjingtech.boot.app.kmp.ui.preview.*

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentSurface
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassSurface
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Surface

@Composable
fun BootSurface(modifier: Modifier = Modifier, cornerRadius: Dp = 16.dp, content: @Composable () -> Unit = {}) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassSurface(modifier = modifier, cornerRadius = cornerRadius, content = content)
        BootUiStyle.MATERIAL3 -> Material3Surface(modifier = modifier, cornerRadius = cornerRadius, content = content)
        BootUiStyle.FLUENT -> FluentSurface(modifier = modifier, cornerRadius = cornerRadius, content = content)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootSurfaceLiquidGlassPreview() {
    Column {
        BootSurface(modifier = Modifier.fillMaxWidth()) { Text("Surface Content") }
        BootSurface(modifier = Modifier.fillMaxWidth(), cornerRadius = 8.dp) { Text("Small Radius Surface") }
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootSurfaceMaterial3StylePreview() {
    Column {
        BootSurface(modifier = Modifier.fillMaxWidth()) { Text("Surface Content") }
        BootSurface(modifier = Modifier.fillMaxWidth(), cornerRadius = 8.dp) { Text("Small Radius") }
    }
}
