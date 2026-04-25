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
fun BootElevatedCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass ElevatedCard")
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
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass OutlinedCard")
        BootUiStyle.MATERIAL3 -> Material3OutlinedCard(
            modifier = modifier,
            content = content
        )
    }
}

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
