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
fun BootTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass TopAppBar")
        BootUiStyle.MATERIAL3 -> Material3TopAppBar(
            title = title,
            modifier = modifier,
            navigationIcon = navigationIcon,
            onNavigationClick = onNavigationClick,
            actions = actions
        )
    }
}

@Composable
fun BootBottomAppBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass BottomAppBar")
        BootUiStyle.MATERIAL3 -> Material3BottomAppBar(
            modifier = modifier,
            content = content
        )
    }
}

@Composable
fun BootNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<Pair<ImageVector, String>> = emptyList()
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass NavigationBar")
        BootUiStyle.MATERIAL3 -> Material3NavigationBar(
            selectedIndex = selectedIndex,
            onItemSelected = onItemSelected,
            modifier = modifier,
            items = items
        )
    }
}

@Composable
fun BootNavigationRail(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<Pair<ImageVector, String>> = emptyList()
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass NavigationRail")
        BootUiStyle.MATERIAL3 -> Material3NavigationRail(
            selectedIndex = selectedIndex,
            onItemSelected = onItemSelected,
            modifier = modifier,
            items = items
        )
    }
}

@Composable
fun BootTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    tabTitles: List<String> = emptyList(),
    onTabSelected: (Int) -> Unit = {}
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass TabRow")
        BootUiStyle.MATERIAL3 -> Material3TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = modifier,
            tabTitles = tabTitles,
            onTabSelected = onTabSelected
        )
    }
}

@Preview
@Composable
private fun BootNavigationPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column {
                BootTopAppBar(
                    title = "Title",
                    navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                    onNavigationClick = {},
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Search, "Search")
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Settings, "Settings")
                        }
                    }
                )
                var sel by remember { mutableIntStateOf(0) }
                BootNavigationBar(
                    selectedIndex = sel,
                    onItemSelected = { sel = it },
                    items = listOf(
                        Icons.Default.Home to "Home",
                        Icons.Default.Favorite to "Favorites",
                        Icons.Default.Person to "Profile"
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun BootTabRowPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            var sel by remember { mutableIntStateOf(0) }
            Column {
                BootTabRow(
                    selectedTabIndex = sel,
                    tabTitles = listOf("Home", "Feed", "Profile"),
                    onTabSelected = { sel = it }
                )
                Text(
                    "Selected: ${listOf("Home", "Feed", "Profile")[sel]}",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
