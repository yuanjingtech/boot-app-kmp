package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
    //
    // ⚠️⚠️ 加载状态与页面标题**必须拆成两个 `LaunchedEffect`**：
    // `snapshotFlow{}.collect{}` **永不返回** ⇒ 写在同一个 `LaunchedEffect` 里的第二个
    // `collect` **永远不会被执行** ⇒ ⇒ `updatePageTitle` 永远不会被调用
    // ⇒ ⇒ `WebViewState.pageTitle` 恒为 null ⇒ KMP 侧的页面标题永远不显示。
    // （这个缺陷在 2026-10-24 被 @远景react native开发 核出来并记进 `tasks.md` 的 T2.6。）
    //
    // 拆出来的理由、进度同步的口径见 `WebViewStateSyncEffect` 的 KDoc。
    WebViewStateSyncEffect(libraryState = state, stateHolder = stateHolder)

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

    // Platform → UI: 同步加载进度（T2.8）。
    //
    // ⚠️ `WebViewState.updateProgress(...)` 早就有，但全仓**调用点数为 0**
    // ⇒ `progress` 恒为 0 ⇒ 进度条永远停在 0%、加载完也不消失。
    //
    // ⚠️ 读取必须写在**这个模块**（`webview-parkwoocheol`）：只有它依赖 `compose-webview`
    // ⇒ 库的 `LoadingState` 类型不能透到 `webview` 模块（否则要把库提到 compile scope）。
    //
    // ⚠️ **各平台能读到的值不同**：Android / iOS 有真进度（0~1）；
    // desktop / js / wasmJs 的库**只写 `Loading(0f)`** ⇒ 取不到真值
    // ⇒ ⇒ 那些平台上 `progress` 恒为 0 ⇒ **进度条不显示**
    // （满足 T2.8 的验收标准「要么消失、要么显示真实进度」）。
    val progressSource = remember(state) {
        LibraryProgressSource(libraryState = state)
    }
    ProgressStateEffect(progressSource, stateHolder)

    // Platform → UI: 同步加载失败（给"缓存条目失效后从列表移除"用）。
    //
    // 库的 `ComposeWebView` 有 `errorContent: @Composable (List<WebViewError>) -> Unit` 这个
    // **composable slot**（⚠️ **不是** `onWebError`，也不是普通回调）
    // ⇒ 加载失败时库会把错误递出来
    // ⇒ 但 boot 的 `WebViewState.error` 永远为 null ⇒ 消费方拿不到失败信号。
    //
    // ⚠️ 库的 `WebViewError` 类型**不能**透出去：boot 的 `webview` 模块
    // （`WebViewState` 所在处）**没有**依赖 `compose-webview`
    // ⇒ 一旦透出去，KMP 侧就得把它提到 compile scope ⇒ 不必要地扩大依赖。
    val loadErrorSource = remember { MutableLoadErrorSource() }
    LoadErrorEffect(loadErrorSource, stateHolder)

    // Platform → UI: **新一轮加载开始时**把上一次的加载失败清掉。
    //
    // ⚠️ 必须走 client 的 `onPageStarted` 回调：库的 `errorContent` slot
    // **只在列表非空时才会被调用**
    // ⇒ ⇒ 永远递不到空列表 ⇒ 靠 slot 自己是清不掉的（详见
    // `createLoadErrorClearingClient` 的 KDoc）。
    val client = remember(loadErrorSource) {
        createLoadErrorClearingClient(loadErrorSource)
    }

    // UI → Platform: 响应外部 url 变化，加载页面
    LaunchedEffect(stateHolder.url) {
        controller.loadUrl(stateHolder.url)
    }

    // Platform → UI: **把 controller 挂到 state holder 上**（T2.7）。
    //
    // ⚠️ 为什么必须挂：
    // boot 的 `WebViewState` 在 T2.7 之前**没有任何途径**发起前进/后退
    // （`WebViewController` 接口是悬空契约）⇒ ⇒ 消费方（KMP 的两个子应用屏）
    // 的"前进/后退工具栏"点了也没东西可调 ⇒ ⇒ 必须把 controller 递上去。
    //
    // ⚠️ 为什么用 `DisposableEffect` 而不是 `LaunchedEffect`：
    // `LaunchedEffect` 的 `onDispose` 在**协程结束**时才跑，而这里要做的是
    // "这个组合还在不在" ⇒ ⇒ `DisposableEffect` 才是对的。
    //
    // ⚠️ 为什么 `stateHolder` 要进 key：
    // state holder 换了（`remember(app.url)` 重建）⇒ ⇒ 必须把旧的摘掉、把新的挂上
    // ⇒ ⇒ 否则会拿着一个已经失效的 controller 继续调。
    DisposableEffect(controller, stateHolder) {
        val adapter = LibraryWebViewController(controller)
        stateHolder.attachController(adapter)
        onDispose {
            stateHolder.detachController()
        }
    }

    ComposeWebView(
        modifier = modifier,
        state = state,
        controller = controller,
        client = client,
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

