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
fun BootFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String = "Add"
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass FloatingActionButton")
        BootUiStyle.MATERIAL3 -> Material3FloatingActionButton(
            onClick = onClick,
            modifier = modifier,
            icon = icon,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun BootSmallFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String = "Add"
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass SmallFloatingActionButton")
        BootUiStyle.MATERIAL3 -> Material3SmallFloatingActionButton(
            onClick = onClick,
            modifier = modifier,
            icon = icon,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun BootLargeFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String = "Add"
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass LargeFloatingActionButton")
        BootUiStyle.MATERIAL3 -> Material3LargeFloatingActionButton(
            onClick = onClick,
            modifier = modifier,
            icon = icon,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun BootExtendedFloatingActionButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    expanded: Boolean = true
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass ExtendedFloatingActionButton")
        BootUiStyle.MATERIAL3 -> Material3ExtendedFloatingActionButton(
            onClick = onClick,
            text = text,
            modifier = modifier,
            icon = icon,
            expanded = expanded
        )
    }
}

@Preview
@Composable
private fun BootFABPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BootSmallFloatingActionButton(
                    onClick = {},
                    icon = Icons.Default.Add
                )
                BootFloatingActionButton(
                    onClick = {},
                    icon = Icons.Default.Add
                )
                BootLargeFloatingActionButton(
                    onClick = {},
                    icon = Icons.Default.Add
                )
                BootExtendedFloatingActionButton(
                    onClick = {},
                    text = "Add Item",
                    icon = Icons.Default.Add
                )
            }
        }
    }
}
