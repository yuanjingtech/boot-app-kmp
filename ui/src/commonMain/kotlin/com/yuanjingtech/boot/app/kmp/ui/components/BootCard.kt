package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassCard
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Card

@Composable
fun BootCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassCard(modifier = modifier, content = content)
        BootUiStyle.MATERIAL3 -> Material3Card(modifier = modifier, content = content)
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@BootPreviews
@Composable
private fun BootCardPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides LocalUiStyle.current) {
            BootCard { Text("Card Content") }
        }
    }
}

// ─── Legacy single-style previews ────────────────────────────────────────────

@Preview
@Composable
private fun BootCardLiquidGlassPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {
            BootCard { Text("LiquidGlass Card Content") }
        }
    }
}

@Preview
@Composable
private fun BootCardMaterial3Preview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            BootCard { Text("Material3 Card Content") }
        }
    }
}
