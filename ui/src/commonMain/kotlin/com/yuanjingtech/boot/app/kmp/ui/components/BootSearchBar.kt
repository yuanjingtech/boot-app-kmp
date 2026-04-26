package com.yuanjingtech.boot.app.kmp.ui.components

import com.yuanjingtech.boot.app.kmp.ui.preview.*

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.*
import com.yuanjingtech.boot.app.kmp.ui.material3.Material3SearchBar

@Composable
fun BootSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass SearchBar")
        BootUiStyle.MATERIAL3 -> Material3SearchBar(query = query, onQueryChange = onQueryChange, onSearch = onSearch, active = active, onActiveChange = onActiveChange, modifier = modifier, placeholder = placeholder, leadingIcon = leadingIcon ?: Icons.Default.Search, trailingIcon = trailingIcon)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootSearchBarLiquidGlassPreview() {
    var q by remember { mutableStateOf("") }
    var a by remember { mutableStateOf(false) }
    BootSearchBar(query = q, onQueryChange = { q = it }, onSearch = { a = false }, active = a, onActiveChange = { a = it })
}

@Preview
@Composable
@PreviewWrapper(wrapper = Material3PreviewWrapper::class)
private fun BootSearchBarMaterial3Preview() {
    var q by remember { mutableStateOf("") }
    var a by remember { mutableStateOf(false) }
    BootSearchBar(query = q, onQueryChange = { q = it }, onSearch = { a = false }, active = a, onActiveChange = { a = it })
}
