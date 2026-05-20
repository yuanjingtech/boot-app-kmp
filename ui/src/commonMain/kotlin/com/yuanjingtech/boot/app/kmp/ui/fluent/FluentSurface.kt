package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper

@Composable
fun FluentSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
    content: @Composable () -> Unit,
) {
    val colors = LocalFluentColors.current
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .background(colors.surface, shape),
        contentAlignment = Alignment.TopStart,
    ) {
        content()
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentSurfacePreview() {
    FluentSurface {
        FluentText("Surface Content")
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentSurfaceLightPreview() {
    FluentSurface(cornerRadius = 12.dp) {
        FluentText("Custom Corner Radius")
    }
}