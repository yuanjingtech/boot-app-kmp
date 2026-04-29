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

// ─── Material3Divider ─────────────────────────────────────────────────────────
@Composable
fun Material3HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
fun Material3VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp
) {
    VerticalDivider(
        modifier = modifier,
        thickness = thickness,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

// ─── Material3ListItem ─────────────────────────────────────────────────────
@Composable
fun Material3ListItem(
    headlineContent: String,
    modifier: Modifier = Modifier,
    overlineContent: String = "",
    supportingContent: String = "",
    leadingContent: ImageVector? = null,
    trailingContent: String = ""
) {
    ListItem(
        headlineContent = { Text(headlineContent) },
        overlineContent = if (overlineContent.isNotEmpty()) { { Text(overlineContent) } } else null,
        supportingContent = if (supportingContent.isNotEmpty()) { { Text(supportingContent) } } else null,
        leadingContent = leadingContent?.let { { Icon(it, null) } },
        trailingContent = if (trailingContent.isNotEmpty()) { { Text(trailingContent) } } else null,
        modifier = modifier
    )
}

@Preview
@Composable
private fun Material3DividerPreview() {
    MaterialTheme {
        Column(Modifier.padding(8.dp)) {
            Text("Above divider")
            Material3HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text("Below divider")
            Row(Modifier.height(80.dp)) {
                Text("Left")
                Material3VerticalDivider(Modifier.padding(horizontal = 8.dp))
                Text("Right")
            }
        }
    }
}

@Preview
@Composable
private fun Material3ListItemPreview() {
    MaterialTheme {
        Column(Modifier.padding(8.dp)) {
            Material3ListItem(
                headlineContent = "Headline Text",
                overlineContent = "OVERLINE",
                supportingContent = "Supporting text",
                leadingContent = Icons.Default.Person,
                trailingContent = "Trailing"
            )
            Material3HorizontalDivider()
            Material3ListItem(headlineContent = "Simple Item", leadingContent = Icons.Default.Email)
        }
    }
}
