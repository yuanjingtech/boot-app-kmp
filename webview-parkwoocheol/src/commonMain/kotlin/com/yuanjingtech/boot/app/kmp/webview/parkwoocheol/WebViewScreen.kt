package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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

    // Platform → UI: 同步导航状态（能否前进/后退）。
    //
    // `compose-webview` 的默认 `ComposeWebViewClient` 在 `onPageStarted` / `onPageFinished`
    // 里已经把 `canGoBack` / `canGoForward` 写回了 controller
    // （字节码可见 `WebViewController.setCanGoBack$compose_webview` 调用），
    // 且这两个 getter 是 public 的 ⇒ 这里只需**转发**给 state holder。
    //
    // ⚠️ 不这么做的话，`WebViewState.canGoBack` 恒为 false
    // ⇒ 消费方拿不到真实值 ⇒ 返回键拦截 / 前进后退按钮都无法启用。
    val navigationSource = remember(controller) {
        object : NavigationStateSource {
            override val canGoBack: Boolean get() = controller.canGoBack
            override val canGoForward: Boolean get() = controller.canGoForward
        }
    }
    NavigationStateEffect(navigationSource, stateHolder)

    // Platform → UI: 同步加载失败（给"缓存条目失效后从列表移除"用）。
    //
    // 库的 `ComposeWebView` 有 `onWebError: (List<WebViewError>) -> Unit` 参数
    // ⇒ 加载失败时库会把错误递出来
    // ⇒ 但 boot 的 `WebViewState.error` 永远为 null ⇒ 消费方拿不到失败信号。
    //
    // ⚠️ 库的 `WebViewError` 类型**不能**透出去：boot 的 `webview` 模块
    // （`WebViewState` 所在处）**没有**依赖 `compose-webview`
    // ⇒ 一旦透出去，KMP 侧就得把它提到 compile scope ⇒ 不必要地扩大依赖。
    val loadErrorSource = remember { MutableLoadErrorSource() }
    LoadErrorEffect(loadErrorSource, stateHolder)

    // UI → Platform: 响应外部 url 变化，加载页面
    LaunchedEffect(stateHolder.url) {
        controller.loadUrl(stateHolder.url)
    }

    ComposeWebView(
        modifier = modifier,
        state = state,
        controller = controller,
        errorContent = { errors ->
            applyLoadErrors(loadErrorSource, errors)
        },
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

