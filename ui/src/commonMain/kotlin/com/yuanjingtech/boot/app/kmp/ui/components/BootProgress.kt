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
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.*
import com.yuanjingtech.boot.app.kmp.ui.material3.*

@Composable
fun BootCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = Color.Unspecified
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass CircularProgressIndicator")
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
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass LinearProgressIndicator")
        BootUiStyle.MATERIAL3 -> Material3LinearProgressIndicator(
            modifier = modifier,
            progress = progress
        )
    }
}

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
