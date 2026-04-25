package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassCard
import com.yuanjingtech.boot.app.kmp.ui.material3.*

@Composable
fun BootElevatedCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassCard(
            modifier = modifier,
            content = content
        )
        BootUiStyle.MATERIAL3 -> Material3ElevatedCard(
            modifier = modifier,
            content = content
        )
    }
}

@Composable
fun BootOutlinedCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassCard(
            modifier = modifier,
            content = content
        )
        BootUiStyle.MATERIAL3 -> Material3OutlinedCard(
            modifier = modifier,
            content = content
        )
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@Preview(name = "LiquidGlass", group = "UI Style")
@Preview(name = "Material3", group = "UI Style")
annotation class BootCardsStylePreviews

@Preview(name = "Elevated", group = "Variant")
@Preview(name = "Outlined", group = "Variant")
annotation class BootCardsVariantPreviews

@BootCardsStylePreviews
@Composable
private fun BootCardsStylePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BootElevatedCard { Text("Elevated Card") }
                BootOutlinedCard { Text("Outlined Card") }
            }
        }
    }
}

@BootCardsVariantPreviews
@Composable
private fun BootCardsVariantPreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BootElevatedCard { Text("Elevated Card") }
                BootOutlinedCard { Text("Outlined Card") }
            }
        }
    }
}

// ─── Legacy single-style preview ──────────────────────────────────────────────

@Preview
@Composable
private fun BootCardVariantsPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BootElevatedCard { Text("Elevated Card") }
                BootOutlinedCard { Text("Outlined Card") }
            }
        }
    }
}
