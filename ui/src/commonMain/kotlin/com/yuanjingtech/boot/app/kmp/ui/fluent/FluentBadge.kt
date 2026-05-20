package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper

@Composable
fun FluentBadge(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF0078D4),
    contentColor: Color = Color.White,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = LocalFluentColors.current
    Box(
        modifier = modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Row { content() }
    }
}

@Composable
fun FluentBadgedBox(
    badgeContent: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        content()
        Box(
            modifier = Modifier.align(Alignment.TopEnd),
            content = badgeContent,
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentBadgePreview() {
    FluentBadgedBox(
        badgeContent = { FluentBadge { Text("3") } },
    ) {
        Box(modifier = Modifier.size(100.dp).padding(8.dp)) {
            Text("Content")
        }
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentBadgeLightPreview() {
    FluentBadgedBox(
        badgeContent = { FluentBadge(backgroundColor = Color(0xFF0078D4)) { Text("99+") } },
    ) {
        Box(modifier = Modifier.size(100.dp).padding(8.dp)) {
            Text("Notifications")
        }
    }
}