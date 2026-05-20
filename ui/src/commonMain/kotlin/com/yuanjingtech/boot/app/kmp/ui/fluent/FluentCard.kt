package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper

@Composable
fun FluentCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalFluentColors.current
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .background(colors.surface, shape)
            .padding(16.dp),
    ) {
        Column(content = content)
    }
}

@Composable
fun FluentElevatedCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalFluentColors.current
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .background(colors.surfaceElevated, shape)
            .padding(16.dp),
    ) {
        Column(content = content)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentCardPreview() {
    FluentCard {
        Text("Card Content")
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentElevatedCardPreview() {
    FluentElevatedCard {
        Text("Elevated Card")
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentCardLightPreview() {
    FluentCard {
        Text("Light Theme Card")
    }
}