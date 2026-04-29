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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

// ─── Material3SearchBar ──────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Material3SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    leadingIcon: ImageVector = Icons.Default.Search,
    trailingIcon: ImageVector? = null
) {
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                expanded = active,
                onExpandedChange = onActiveChange,
                placeholder = { Text(placeholder) },
                leadingIcon = { Icon(leadingIcon, null) },
                trailingIcon = trailingIcon?.let { { Icon(it, null) } }
            )
        },
        expanded = active,
        onExpandedChange = onActiveChange,
        modifier = modifier,
        content = {
            Column(Modifier.padding(16.dp)) {
                Text("Recent searches")
                ListItem(headlineContent = { Text("Kotlin") }, leadingContent = { Icon(Icons.Default.Search, null) })
                ListItem(headlineContent = { Text("Compose") }, leadingContent = { Icon(Icons.Default.Search, null) })
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Material3SearchBarPreview() {
    MaterialTheme {
        var q by remember { mutableStateOf("") }
        var a by remember { mutableStateOf(false) }
        Material3SearchBar(
            query = q,
            onQueryChange = { q = it },
            onSearch = { a = false },
            active = a,
            onActiveChange = { a = it }
        )
    }
}
