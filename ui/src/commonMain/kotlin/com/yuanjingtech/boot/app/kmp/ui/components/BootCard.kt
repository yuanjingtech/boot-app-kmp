package com.yuanjingtech.boot.app.kmp.ui.components

import com.yuanjingtech.boot.app.kmp.ui.preview.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassCard
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Card

@Composable
fun BootCard(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassCard(
            modifier = modifier,
            isLoading = isLoading,
            content = content,
        )
        BootUiStyle.MATERIAL3 -> Material3Card(
            modifier = modifier,
            isLoading = isLoading,
            content = content,
        )
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootCardLiquidGlassPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BootCard { Text("Card Content") }
        BootCard(isLoading = true) { Text("Loading...") }
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootCardMaterial3Preview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BootCard { Text("Card Content") }
        BootCard(isLoading = true) { Text("Loading...") }
    }
}
