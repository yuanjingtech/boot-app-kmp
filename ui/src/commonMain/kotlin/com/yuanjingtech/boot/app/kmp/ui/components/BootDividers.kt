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
fun BootHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass HorizontalDivider")
        BootUiStyle.MATERIAL3 -> Material3HorizontalDivider(
            modifier = modifier,
            thickness = thickness
        )
    }
}

@Composable
fun BootVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass VerticalDivider")
        BootUiStyle.MATERIAL3 -> Material3VerticalDivider(
            modifier = modifier,
            thickness = thickness
        )
    }
}

@Composable
fun BootListItem(
    headlineContent: String,
    modifier: Modifier = Modifier,
    overlineContent: String = "",
    supportingContent: String = "",
    leadingContent: ImageVector? = null,
    trailingContent: String = ""
) {
    when (LocalUiStyle.current) {
        BootUiStyle.LIQUID_GLASS -> TODO("LiquidGlass ListItem")
        BootUiStyle.MATERIAL3 -> Material3ListItem(
            headlineContent = headlineContent,
            modifier = modifier,
            overlineContent = overlineContent,
            supportingContent = supportingContent,
            leadingContent = leadingContent,
            trailingContent = trailingContent
        )
    }
}

// ─── Multi-Preview Annotations ────────────────────────────────────────────────

@Preview(name = "LiquidGlass", group = "UI Style")
@Preview(name = "Material3", group = "UI Style")
annotation class BootDividersStylePreviews

@Preview(name = "Horizontal", group = "Type")
@Preview(name = "Vertical", group = "Type")
@Preview(name = "ListItem", group = "Type")
annotation class BootDividersTypePreviews

@BootDividersStylePreviews
@Composable
private fun BootDividersStylePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Above")
                BootHorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Below")
                Row(modifier = Modifier.height(80.dp)) {
                    Text("Left")
                    BootVerticalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    Text("Right")
                }
                BootListItem(
                    headlineContent = "Headline",
                    overlineContent = "OVERLINE",
                    supportingContent = "Supporting",
                    leadingContent = Icons.Default.Person,
                    trailingContent = "Trailing"
                )
            }
        }
    }
}

@BootDividersTypePreviews
@Composable
private fun BootDividersTypePreview(
    @PreviewParameter(BootUiStyleProvider::class) style: BootUiStyle
) {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides style) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Above")
                BootHorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Below")
                Row(modifier = Modifier.height(80.dp)) {
                    Text("Left")
                    BootVerticalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    Text("Right")
                }
                BootListItem(
                    headlineContent = "Headline",
                    leadingContent = Icons.Default.Email
                )
            }
        }
    }
}

// ─── Legacy single-style previews ─────────────────────────────────────────────

@Preview
@Composable
private fun BootDividersPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Above")
                BootHorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Below")
                Row(modifier = Modifier.height(80.dp)) {
                    Text("Left")
                    BootVerticalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    Text("Right")
                }
            }
        }
    }
}

@Preview
@Composable
private fun BootListItemPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            Column(modifier = Modifier.padding(8.dp)) {
                BootListItem(
                    headlineContent = "Headline",
                    overlineContent = "OVERLINE",
                    supportingContent = "Supporting",
                    leadingContent = Icons.Default.Person,
                    trailingContent = "Trailing"
                )
                BootHorizontalDivider()
                BootListItem(
                    headlineContent = "Simple",
                    leadingContent = Icons.Default.Email
                )
            }
        }
    }
}
