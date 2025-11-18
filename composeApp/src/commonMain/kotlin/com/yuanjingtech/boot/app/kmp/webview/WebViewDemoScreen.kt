package com.yuanjingtech.boot.app.kmp.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WebViewDemoScreen(
    modifier: Modifier = Modifier,
) {
    WebViewScreen(
        modifier = modifier,
        url = "https://yuanjingtech.github.io/privancy/"
    )
}