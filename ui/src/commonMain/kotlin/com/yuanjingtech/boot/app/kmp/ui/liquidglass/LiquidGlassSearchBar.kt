package com.yuanjingtech.boot.app.kmp.ui.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

private val defaultSuggestions = listOf("Kotlin", "Compose Multiplatform", "Material3", "LiquidGlass")

@Composable
fun LiquidGlassSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    leadingIcon: ImageVector = Icons.Default.Search,
    trailingIcon: ImageVector? = null,
) {
    val colors = LocalLiquidGlassColors.current
    val focusManager = LocalFocusManager.current
    val suggestions = defaultSuggestions

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .liquidGlassSurface(cornerRadius = 28.dp, borderAlpha = 0.22f)
                .padding(horizontal = 4.dp, vertical = 4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = colors.secondary.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = TextStyle(color = colors.content.copy(alpha = colors.contentAlpha)),
                    cursorBrush = SolidColor(colors.content),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch(query)
                            focusManager.clearFocus()
                        },
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            if (!it.hasFocus && query.isEmpty()) {
                                onActiveChange(false)
                            }
                        },
                    decorationBox = { innerTextField ->
                        Box {
                            if (query.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = colors.secondary.copy(alpha = colors.secondaryAlpha),
                                )
                            }
                            innerTextField()
                        }
                    },
                )
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = { onQueryChange("") },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = colors.secondary.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
                if (trailingIcon != null) {
                    IconButton(
                        onClick = { onSearch(query) },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = "Search",
                            tint = colors.secondary.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }

        if (active) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .liquidGlassSurface(cornerRadius = 16.dp, borderAlpha = 0.18f)
                    .padding(vertical = 8.dp),
            ) {
                Text(
                    text = "Suggestions",
                    color = colors.secondary.copy(alpha = colors.secondaryAlpha),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                suggestions.forEach { suggestion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onQueryChange(suggestion)
                                onSearch(suggestion)
                                focusManager.clearFocus()
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = colors.secondary.copy(alpha = colors.secondaryAlpha),
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = suggestion,
                            color = colors.content.copy(alpha = colors.contentAlpha),
                        )
                    }
                }
            }
        }
    }
}
