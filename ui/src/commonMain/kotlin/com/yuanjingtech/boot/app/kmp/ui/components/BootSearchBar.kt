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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.*
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.*
import com.yuanjingtech.boot.app.kmp.ui.material3.*

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
        BootUiStyle.MATERIAL3 -> Material3SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = active,
            onActiveChange = onActiveChange,
            modifier = modifier,
            placeholder = placeholder,
            leadingIcon = leadingIcon ?: Icons.Default.Search,
            trailingIcon = trailingIcon
        )
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@Preview(name = "LiquidGlass", group = "UI Style")
@Preview(name = "Material3", group = "UI Style")
annotation class BootSearchBarStylePreviews

@Preview(name = "Inactive", group = "State")
@Preview(name = "Active", group = "State")
annotation class BootSearchBarStatePreviews

@BootSearchBarStylePreviews
@Composable
private fun BootSearchBarStylePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            var q by remember { mutableStateOf("") }
            var a by remember { mutableStateOf(false) }
            BootSearchBar(
                query = q,
                onQueryChange = { q = it },
                onSearch = { a = false },
                active = a,
                onActiveChange = { a = it }
            )
        }
    }
}

@BootSearchBarStatePreviews
@Composable
private fun BootSearchBarStatePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            var q by remember { mutableStateOf("") }
            var a by remember { mutableStateOf(false) }
            BootSearchBar(
                query = q,
                onQueryChange = { q = it },
                onSearch = { a = false },
                active = a,
                onActiveChange = { a = it }
            )
        }
    }
}

// ─── Legacy single-style preview ──────────────────────────────────────────────

@Preview
@Composable
private fun BootSearchBarPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            var q by remember { mutableStateOf("") }
            var a by remember { mutableStateOf(false) }
            BootSearchBar(
                query = q,
                onQueryChange = { q = it },
                onSearch = { a = false },
                active = a,
                onActiveChange = { a = it }
            )
        }
    }
}
