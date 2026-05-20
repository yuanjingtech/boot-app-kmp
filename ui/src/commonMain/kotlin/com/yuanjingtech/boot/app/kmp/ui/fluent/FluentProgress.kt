package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper

@Composable
fun FluentCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color? = null,
) {
    val colors = LocalFluentColors.current
    val indicatorColor = color ?: colors.accent

    if (progress != null) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = modifier,
            color = indicatorColor,
            strokeWidth = 3.dp,
            trackColor = colors.controlFillTertiary,
        )
    } else {
        CircularProgressIndicator(
            modifier = modifier,
            color = indicatorColor,
            strokeWidth = 3.dp,
        )
    }
}

@Composable
fun FluentLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
) {
    val colors = LocalFluentColors.current

    if (progress != null) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = modifier,
            color = colors.accent,
            trackColor = colors.controlFillTertiary,
        )
    } else {
        LinearProgressIndicator(
            modifier = modifier,
            color = colors.accent,
            trackColor = colors.controlFillTertiary,
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentProgressIndicatorsPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        FluentCircularProgressIndicator()
        FluentCircularProgressIndicator(progress = 0.7f)
        FluentLinearProgressIndicator()
        FluentLinearProgressIndicator(progress = 0.5f)
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentProgressIndicatorsLightPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        FluentCircularProgressIndicator()
        FluentLinearProgressIndicator(progress = 0.6f)
    }
}