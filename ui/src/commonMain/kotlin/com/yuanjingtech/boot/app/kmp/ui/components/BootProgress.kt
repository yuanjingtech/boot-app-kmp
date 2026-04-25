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
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassCircularProgressIndicator
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassLinearProgressIndicator
import com.yuanjingtech.boot.app.kmp.ui.material3.*

@Composable
fun BootCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = Color.Unspecified
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassCircularProgressIndicator(
            modifier = modifier,
            progress = progress,
            color = color
        )
        BootUiStyle.MATERIAL3 -> Material3CircularProgressIndicator(
            modifier = modifier,
            progress = progress,
            color = color
        )
    }
}

@Composable
fun BootLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassLinearProgressIndicator(
            modifier = modifier,
            progress = progress
        )
        BootUiStyle.MATERIAL3 -> Material3LinearProgressIndicator(
            modifier = modifier,
            progress = progress
        )
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@Preview(name = "LiquidGlass", group = "UI Style")
@Preview(name = "Material3", group = "UI Style")
annotation class BootProgressStylePreviews

@Preview(name = "Circular", group = "Type")
@Preview(name = "Linear", group = "Type")
annotation class BootProgressTypePreviews

@Preview(name = "Indeterminate", group = "Progress")
@Preview(name = "Determinate", group = "Progress")
annotation class BootProgressValuePreviews

@BootProgressStylePreviews
@Composable
private fun BootProgressStylePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BootCircularProgressIndicator()
                BootCircularProgressIndicator(progress = 0.6f)
                BootLinearProgressIndicator()
                BootLinearProgressIndicator(progress = 0.4f)
            }
        }
    }
}

@BootProgressTypePreviews
@Composable
private fun BootProgressTypePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BootCircularProgressIndicator()
                BootLinearProgressIndicator()
            }
        }
    }
}

@BootProgressValuePreviews
@Composable
private fun BootProgressValuePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BootCircularProgressIndicator()
                BootCircularProgressIndicator(progress = 0.6f)
                BootLinearProgressIndicator()
                BootLinearProgressIndicator(progress = 0.4f)
            }
        }
    }
}

// ─── Legacy single-style preview ──────────────────────────────────────────────

@Preview
@Composable
private fun BootProgressPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BootCircularProgressIndicator()
                BootCircularProgressIndicator(progress = 0.6f)
                BootLinearProgressIndicator()
                BootLinearProgressIndicator(progress = 0.4f)
            }
        }
    }
}
