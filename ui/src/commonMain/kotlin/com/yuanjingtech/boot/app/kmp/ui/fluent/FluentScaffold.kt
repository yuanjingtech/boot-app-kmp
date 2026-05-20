package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentLightPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ui.preview.FluentPreviewWrapper

@Composable
fun FluentScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = { _ -> },
) {
    val colors = LocalFluentColors.current
    Column(modifier = modifier.background(colors.background)) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            content(PaddingValues(0.dp))
        }
        topBar()
        bottomBar()
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview
@Composable
@PreviewWrapper(wrapper = FluentPreviewWrapper::class)
private fun FluentScaffoldPreview() {
    FluentScaffold(
        topBar = {
            FluentTopAppBar(title = "App Title")
        },
        bottomBar = {
            FluentBottomAppBar {
                FluentText("Bottom Bar")
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            FluentText("Content Area")
        }
    }
}

@Preview
@Composable
@PreviewWrapper(wrapper = FluentLightPreviewWrapper::class)
private fun FluentScaffoldLightPreview() {
    FluentScaffold(
        topBar = {
            FluentTopAppBar(title = "Light Theme")
        },
        bottomBar = {
            FluentBottomAppBar {
                FluentText("Bottom Bar")
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            FluentText("Light Theme Content")
        }
    }
}