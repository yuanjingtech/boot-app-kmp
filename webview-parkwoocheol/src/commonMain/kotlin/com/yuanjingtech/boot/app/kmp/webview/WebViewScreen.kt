package com.yuanjingtech.boot.app.kmp.webview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.parkwoocheol.composewebview.ComposeWebView
import com.parkwoocheol.composewebview.rememberSaveableWebViewState
import com.parkwoocheol.composewebview.rememberWebViewController
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    url: String,
) {
    val state = rememberSaveableWebViewState(url = url)
    val controller = rememberWebViewController()

    ComposeWebView(
        modifier = modifier,
        state = state,
        controller = controller,
    )
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

@Suppress("unused")
class WebViewScreenImpl : IWebViewScreen {
    override fun content(): @Composable() ((Modifier, WebViewState) -> Unit) = { modifier, state ->
        WebViewScreen(
            modifier = modifier,
            url = state.url
        )
    }
}



