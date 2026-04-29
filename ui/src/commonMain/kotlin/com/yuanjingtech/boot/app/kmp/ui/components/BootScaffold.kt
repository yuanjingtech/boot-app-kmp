package com.yuanjingtech.boot.app.kmp.ui.components
import com.yuanjingtech.boot.app.kmp.ui.preview.*

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassScaffold
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassScaffoldTopBar
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassScaffoldBottomBar
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3Scaffold
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3TopAppBar
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3NavigationBar

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

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootScaffoldLiquidGlassPreview() {
    BootScaffold(
        topBar = {
            LiquidGlassScaffoldTopBar {
                Text("Title", color = Color.White, modifier = Modifier.padding(16.dp))
            }
        },
        bottomBar = {
            LiquidGlassScaffoldBottomBar {
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

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootScaffoldMaterial3Preview() {
    BootScaffold(
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
