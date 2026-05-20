package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper

@Composable
fun FluentSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    leadingIcon: ImageVector = Icons.Default.Search,
    trailingIcon: ImageVector? = null,
) {
    val colors = LocalFluentColors.current
    val shape = RoundedCornerShape(4.dp)
    val backgroundColor = if (active) colors.surface else colors.controlFill
    val textColor = if (active) colors.textPrimary else colors.textSecondary

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, shape)
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(leadingIcon, contentDescription = "Search", tint = textColor)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (query.isEmpty() && !active) {
                        Text(text = placeholder, color = textColor)
                    } else {
                        Text(text = query, color = textColor)
                    }
                }
                trailingIcon?.let { Icon(it, contentDescription = null, tint = textColor) }
            }
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentSearchBarPreview() {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    FluentSearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { },
        active = active,
        onActiveChange = { active = it },
    )
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentSearchBarLightPreview() {
    var query by remember { mutableStateOf("Search query") }
    var active by remember { mutableStateOf(false) }
    FluentSearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { },
        active = active,
        onActiveChange = { active = it },
    )
}