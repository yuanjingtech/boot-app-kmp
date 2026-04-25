package com.yuanjingtech.boot.app.kmp.ui.material3

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
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

// ─── Material3Navigation ────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Material3TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = { if (navigationIcon != null) { IconButton(onClick = onNavigationClick) { Icon(navigationIcon, "Back") } } },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun Material3BottomAppBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        content = content
    )
}

@Composable
fun Material3NavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<Pair<ImageVector, String>> = listOf(
        Icons.Default.Home to "Home",
        Icons.Default.Favorite to "Favorites",
        Icons.Default.Person to "Profile",
        Icons.Default.Settings to "Settings"
    )
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEachIndexed { index, (icon, label) ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = { Icon(icon, label) },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@Composable
fun Material3NavigationRail(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<Pair<ImageVector, String>> = listOf(
        Icons.Default.Home to "Home",
        Icons.Default.Favorite to "Favorites",
        Icons.Default.Person to "Profile",
        Icons.Default.Settings to "Settings"
    )
) {
    NavigationRail(modifier = modifier, containerColor = MaterialTheme.colorScheme.surface) {
        items.forEachIndexed { index, (icon, label) ->
            NavigationRailItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = { Icon(icon, label) },
                label = { Text(label) }
            )
        }
    }
}

@Composable
fun Material3TabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    tabTitles: List<String> = listOf("Tab 1", "Tab 2", "Tab 3"),
    onTabSelected: (Int) -> Unit = {}
) {
    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(title) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun Material3NavigationPreview() {
    MaterialTheme {
        Column {
            Material3TopAppBar(
                title = "Title",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = {},
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") }
                    IconButton(onClick = {}) { Icon(Icons.Default.Settings, "Settings") }
                }
            )
            var sel by remember { mutableIntStateOf(0) }
            Material3NavigationBar(selectedIndex = sel, onItemSelected = { sel = it })
        }
    }
}

@Preview
@Composable
private fun Material3TabRowPreview() {
    MaterialTheme {
        var sel by remember { mutableIntStateOf(0) }
        Column {
            Material3TabRow(
                selectedTabIndex = sel,
                tabTitles = listOf("Home", "Feed", "Profile"),
                onTabSelected = { sel = it }
            )
            Text("Selected: ${listOf("Home", "Feed", "Profile")[sel]}", Modifier.padding(16.dp))
        }
    }
}
