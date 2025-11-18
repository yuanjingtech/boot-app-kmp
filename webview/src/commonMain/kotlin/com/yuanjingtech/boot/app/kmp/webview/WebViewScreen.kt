package com.yuanjingtech.boot.app.kmp.webview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    url: String,
) {
    val state = rememberWebViewState(url)
    Column(
        modifier = modifier,
    ) {
        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        }
        WebView(
            state = state,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
fun WebViewScreenPreview() {
    MaterialTheme {
        WebViewScreen(
            modifier = Modifier.fillMaxSize(),
            url = "https://www.baidu.com"
        )
    }
}