package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper

@Composable
fun FluentHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
) {
    val colors = LocalFluentColors.current
    Box(
        modifier = modifier
            .height(thickness)
            .background(colors.borderDefault),
    )
}

@Composable
fun FluentVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
) {
    val colors = LocalFluentColors.current
    Box(
        modifier = modifier
            .background(colors.borderDefault),
    )
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentDividersPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        FluentHorizontalDivider()
        FluentHorizontalDivider(thickness = 2.dp)
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentDividersLightPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        FluentHorizontalDivider()
        FluentHorizontalDivider(thickness = 2.dp)
    }
}