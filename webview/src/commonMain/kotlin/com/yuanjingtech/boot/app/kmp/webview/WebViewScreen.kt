package com.yuanjingtech.boot.app.kmp.webview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.koinInject

// ============================================================
// WebViewState - 可变状态持有者 (State Holder)
// ============================================================
@Stable
class WebViewState(
    initialUrl: String = "",
) {
    var url by mutableStateOf(initialUrl)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var progress by mutableStateOf(0f)
        private set

    var canGoBack by mutableStateOf(false)
        private set

    var canGoForward by mutableStateOf(false)
        private set

    var error by mutableStateOf<WebViewError?>(null)
        private set

    var pageTitle by mutableStateOf<String?>(null)
        private set

    /** 是否处于可交互状态 */
    val isReady: Boolean
        get() = !isLoading && error == null

    fun updateUrl(url: String) {
        this.url = url
        this.error = null
    }

    fun updateLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun updateProgress(progress: Float) {
        this.progress = progress
    }

    fun updateNavigation(canGoBack: Boolean, canGoForward: Boolean) {
        this.canGoBack = canGoBack
        this.canGoForward = canGoForward
    }

    fun updatePageTitle(title: String?) {
        this.pageTitle = title
    }

    fun updateError(error: WebViewError?) {
        this.error = error
    }

    fun resetError() {
        this.error = null
    }
}

/** WebView 错误信息 */
@Stable
data class WebViewError(
    val code: Int,
    val description: String,
)

// ============================================================
// WebViewController - 单向控制流
// ============================================================
@Stable
interface WebViewController {
    fun loadUrl(url: String)
    fun reload()
    fun goBack()
    fun goForward()
    fun stopLoading()
    fun clearCache()
}

// ============================================================
// WebViewScreen - Compose UI 层
// ============================================================

/**
 * 主入口：外部传入 state holder，支持 ViewModel 集成
 */
@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    state: WebViewState,
) {
    val impl: IWebViewScreen = koinInject<IWebViewScreen>()

    Column(modifier = modifier) {
        // 加载进度条
        if (state.isLoading || state.progress < 1f) {
            LinearProgressIndicator(
                progress = { state.progress },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // 错误提示
        state.error?.let { error ->
            ErrorView(
                error = error,
                onRetry = { state.resetError() },
            )
        }

        // WebView 内容
        Box(modifier = Modifier.fillMaxSize()) {
            impl.content().invoke(Modifier.fillMaxSize(), state)
        }
    }
}

/**
 * 便捷重载：传入 url，自动创建 state holder
 */
@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    url: String,
) {
    WebViewScreen(modifier, remember { WebViewState(initialUrl = url) })
}

@Composable
private fun ErrorView(
    error: WebViewError,
    onRetry: () -> Unit,
) {
    Text(
        text = "${error.code}: ${error.description}",
        color = MaterialTheme.colorScheme.error,
    )
}

@Preview
@Composable
fun WebViewScreenPreview() {
    KoinApplicationPreview(application = { modules() }) {
        MaterialTheme {
            WebViewScreen(
                modifier = Modifier.fillMaxSize(),
                url = "https://www.baidu.com",
            )
        }
    }
}
