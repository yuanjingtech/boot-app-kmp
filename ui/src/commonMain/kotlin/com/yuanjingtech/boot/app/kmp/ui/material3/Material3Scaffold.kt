package com.yuanjingtech.boot.app.kmp.ui.material3

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Material3Scaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        content = content,
        containerColor = MaterialTheme.colorScheme.background,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Material3ScaffoldPreview() {
    MaterialTheme {
        Material3Scaffold(
            topBar = {
                Material3TopAppBar(
                    title = "Title",
                    navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                    onNavigationClick = {},
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") }
                    }
                )
            },
            bottomBar = {
                var sel by remember { mutableIntStateOf(0) }
                Material3NavigationBar(selectedIndex = sel, onItemSelected = { sel = it })
            },
            content = { padding ->
                Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                    Text("Scaffold Content", modifier = Modifier.padding(16.dp))
                }
            }
        )
    }
}
