package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper

@Composable
fun FluentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = LocalFluentColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = if (enabled) colors.accent else colors.controlFillDisabled
    val shape = RoundedCornerShape(4.dp)

    Box(
        modifier = modifier
            .background(backgroundColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.background(Color.Transparent),
            content = content,
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentButtonPreview() {
    FluentButton(onClick = {}) {
        Text("Fluent Button")
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentButtonDisabledPreview() {
    FluentButton(onClick = {}, enabled = false) {
        Text("Disabled Button")
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentButtonLightPreview() {
    FluentButton(onClick = {}) {
        Text("Light Theme Button")
    }
}