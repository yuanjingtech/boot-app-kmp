package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.parkwoocheol.composewebview.ComposeWebView
import com.parkwoocheol.composewebview.WebViewSettings
import com.parkwoocheol.composewebview.rememberSaveableWebViewState
import com.parkwoocheol.composewebview.rememberWebViewController
import com.yuanjingtech.boot.app.kmp.webview.WebViewState
import com.yuanjingtech.boot.app.kmp.webview.parkwoocheol.di.webviewParkWoocheolModule
import org.koin.compose.KoinApplicationPreview


@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    stateHolder: WebViewState,
) {
    val state = rememberSaveableWebViewState(url = stateHolder.url)
    val controller = rememberWebViewController()

    // Platform → UI: 监听状态变化，回调到 state holder
    LaunchedEffect(state) {
        snapshotFlow { state.isLoading }.collect { isLoading ->
            stateHolder.updateLoading(isLoading)
        }
        snapshotFlow { state.pageTitle }.collect { text ->
            stateHolder.updatePageTitle(text)
        }
    }

    // UI → Platform: 响应外部 url 变化，加载页面
    LaunchedEffect(stateHolder.url) {
        controller.loadUrl(stateHolder.url)
    }

    ComposeWebView(
        modifier = modifier,
        state = state,
        controller = controller,
        settings = WebViewSettings(
            javaScriptEnabled = true,
            domStorageEnabled = true
        )
    )
}

@Preview
@Composable
private fun WebViewScreenPreview() {
    KoinApplicationPreview(application = { modules(webviewParkWoocheolModule) }) {
        MaterialTheme {
            WebViewScreen(
                modifier = Modifier.fillMaxSize(),
                stateHolder = WebViewState(initialUrl = "https://www.baidu.com"),
            )
        }
    }
}

