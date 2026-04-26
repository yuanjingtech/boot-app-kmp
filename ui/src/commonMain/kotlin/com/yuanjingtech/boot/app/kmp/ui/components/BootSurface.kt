package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassSurface
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Surface

@Composable
fun BootSurface(modifier: Modifier = Modifier, cornerRadius: Dp = 16.dp, content: @Composable () -> Unit = {}) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassSurface(modifier = modifier, cornerRadius = cornerRadius, content = content)
        BootUiStyle.MATERIAL3 -> Material3Surface(modifier = modifier, cornerRadius = cornerRadius, content = content)
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@StylePreviews
@Composable
private fun BootSurfaceStylePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides LocalUiStyle.current) {
            BootSurface(modifier = Modifier.fillMaxWidth()) { Text("Surface Content") }
        }
    }
}

@Preview(name = "16dp radius", group = "Corner Radius")
@Preview(name = "8dp radius", group = "Corner Radius")
annotation class BootSurfaceRadiusPreviews

@BootSurfaceRadiusPreviews
@Composable
private fun BootSurfaceRadiusPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            BootSurface(modifier = Modifier.fillMaxWidth(), cornerRadius = 8.dp) { Text("Small Radius") }
        }
    }
}

// ─── Legacy single-style previews ────────────────────────────────────────────

@Preview
@Composable
private fun BootSurfaceLiquidGlassPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {
            BootSurface(modifier = Modifier.fillMaxWidth()) { Text("LiquidGlass Surface") }
        }
    }
}

@Preview
@Composable
private fun BootSurfaceMaterial3Preview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            BootSurface(modifier = Modifier.fillMaxWidth()) { Text("Material3 Surface") }
        }
    }
}

@Preview
@Composable
private fun BootSurfaceSmallRadiusPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {
            BootSurface(modifier = Modifier.fillMaxWidth(), cornerRadius = 8.dp) { Text("Small Radius Surface") }
        }
    }
}
