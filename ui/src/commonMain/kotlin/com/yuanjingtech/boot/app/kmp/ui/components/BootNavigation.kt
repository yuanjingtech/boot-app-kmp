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
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassTopAppBar
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassBottomAppBar
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassNavigationBar
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassNavRail
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassTabRow
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3TopAppBar
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3BottomAppBar
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3NavigationBar
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3NavigationRail
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3TabRow

@Composable
fun BootTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassTopAppBar(title = title, modifier = modifier, navigationIcon = navigationIcon, onNavigationClick = onNavigationClick, actions = actions)
        BootUiStyle.MATERIAL3 -> Material3TopAppBar(title = title, modifier = modifier, navigationIcon = navigationIcon, onNavigationClick = onNavigationClick, actions = actions)
    }
}

@Composable
fun BootBottomAppBar(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit = {}) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassBottomAppBar(modifier = modifier, content = content)
        BootUiStyle.MATERIAL3 -> Material3BottomAppBar(modifier = modifier, content = content)
    }
}

@Composable
fun BootNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit, modifier: Modifier = Modifier, items: List<Pair<ImageVector, String>> = emptyList()) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassNavigationBar(selectedIndex = selectedIndex, onItemSelected = onItemSelected, modifier = modifier, items = items)
        BootUiStyle.MATERIAL3 -> Material3NavigationBar(selectedIndex = selectedIndex, onItemSelected = onItemSelected, modifier = modifier, items = items)
    }
}

@Composable
fun BootNavigationRail(selectedIndex: Int, onItemSelected: (Int) -> Unit, modifier: Modifier = Modifier, items: List<Pair<ImageVector, String>> = emptyList()) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassNavRail(selectedIndex = selectedIndex, onItemSelected = onItemSelected, modifier = modifier, items = items)
        BootUiStyle.MATERIAL3 -> Material3NavigationRail(selectedIndex = selectedIndex, onItemSelected = onItemSelected, modifier = modifier, items = items)
    }
}

@Composable
fun BootTabRow(selectedTabIndex: Int, modifier: Modifier = Modifier, tabTitles: List<String> = emptyList(), onTabSelected: (Int) -> Unit = {}) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> LiquidGlassTabRow(selectedTabIndex = selectedTabIndex, modifier = modifier, tabTitles = tabTitles, onTabSelected = onTabSelected)
        BootUiStyle.MATERIAL3 -> Material3TabRow(selectedTabIndex = selectedTabIndex, modifier = modifier, tabTitles = tabTitles, onTabSelected = onTabSelected)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootNavigationLiquidGlassPreview() {
    Column {
        BootTopAppBar(title = "Title", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = {}, actions = {
            IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") }
            IconButton(onClick = {}) { Icon(Icons.Default.Settings, "Settings") }
        })
        var sel by remember { mutableIntStateOf(0) }
        BootNavigationBar(selectedIndex = sel, onItemSelected = { sel = it }, items = listOf(Icons.Default.Home to "Home", Icons.Default.Favorite to "Favorites", Icons.Default.Person to "Profile"))
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootNavigationMaterial3Preview() {
    Column {
        BootTopAppBar(title = "Title", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = {}, actions = {
            IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") }
            IconButton(onClick = {}) { Icon(Icons.Default.Settings, "Settings") }
        })
        var sel by remember { mutableIntStateOf(0) }
        BootNavigationBar(selectedIndex = sel, onItemSelected = { sel = it }, items = listOf(Icons.Default.Home to "Home", Icons.Default.Favorite to "Favorites", Icons.Default.Person to "Profile"))
    }
}
