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
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassScaffold
import com.yuanjingtech.boot.app.kmp.ui.material3.*

@Composable
fun BootScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = { _ -> }
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassScaffold(modifier = modifier, topBar = topBar, bottomBar = bottomBar, content = content)
        BootUiStyle.MATERIAL3 -> Material3Scaffold(modifier = modifier, topBar = topBar, bottomBar = bottomBar, content = content)
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@StylePreviews
@Composable
private fun BootScaffoldStylePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides LocalUiStyle.current) {
            when (LocalUiStyle.current) {
                BootUiStyle.LIQUID_GLASS -> {
                    com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassScaffold(
                        topBar = {
                            com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassScaffoldTopBar {
                                Text("Title", color = Color.White, modifier = Modifier.padding(16.dp))
                            }
                        },
                        bottomBar = {
                            com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassScaffoldBottomBar {
                                Text("Bottom", color = Color.White, modifier = Modifier.padding(16.dp))
                            }
                        },
                        content = { _ ->
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text("Content", color = Color.White, modifier = Modifier.padding(16.dp))
                            }
                        }
                    )
                }
                BootUiStyle.MATERIAL3 -> {
                    Material3Scaffold(
                        topBar = {
                            Material3TopAppBar(title = "Title", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = {})
                        },
                        bottomBar = {
                            var sel by remember { mutableIntStateOf(0) }
                            Material3NavigationBar(selectedIndex = sel, onItemSelected = { sel = it })
                        },
                        content = { padding ->
                            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                                Text("Content", modifier = Modifier.padding(16.dp))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(name = "Empty", group = "Variant")
@Preview(name = "With TopBar", group = "Variant")
@Preview(name = "With BottomBar", group = "Variant")
annotation class BootScaffoldVariantPreviews

@BootScaffoldVariantPreviews
@Composable
private fun BootScaffoldVariantPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Material3Scaffold(
                topBar = { Material3TopAppBar(title = "Title", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = {}) },
                bottomBar = {
                    var sel by remember { mutableIntStateOf(0) }
                    Material3NavigationBar(selectedIndex = sel, onItemSelected = { sel = it })
                },
                content = { padding ->
                    Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                        Text("Content", modifier = Modifier.padding(16.dp))
                    }
                }
            )
        }
    }
}

// ─── Legacy single-style preview ──────────────────────────────────────────────

@Preview
@Composable
private fun BootScaffoldPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Material3Scaffold(
                topBar = { Material3TopAppBar(title = "Title", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = {}) },
                bottomBar = {
                    var sel by remember { mutableIntStateOf(0) }
                    Material3NavigationBar(selectedIndex = sel, onItemSelected = { sel = it })
                },
                content = { padding ->
                    Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                        Text("Content", modifier = Modifier.padding(16.dp))
                    }
                }
            )
        }
    }
}
