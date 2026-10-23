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
 * **进度条该不该显示。**
 *
 * ⚠️ **这个判据被单独抽出来，是为了能直接测它** ——
 * 之前它是一个写在 `if` 里的表达式 ⇒ ⇒ 测不到 ⇒ ⇒ 于是"完成那一刻掉回 0f"
 * 那个 bug 可以悄悄溜过去（**我自己的变异测试就是这么发现的**）。
 *
 * ## ⚠️ 为什么它是 **public**（而不是 `internal`）
 *
 * 实现层（`webview-parkwoocheol`）的测试要**用它**来验"完成那一刻进度条会消失"
 * ⇒ ⇒ 而 `internal` **不跨模块可见** ⇒ ⇒ 只能 public
 * ⇒ ⇒ 好在它本来就是公共契约的一部分（"什么进度该显示进度条"）。
 *
 * ## 判据为什么是 `0f < progress < 1f`
 *
 * ⚠️⚠️ **上界 `< 1f` 是"让进度条能消失"的关键**：
 * 库的 Android / iOS 实现**在进度到 100 时写 `Finished` 而不是 `Loading(1f)`**
 * ⇒ ⇒ 实现层读到的 `Loading.progress` **最大只能到 0.99**
 * ⇒ ⇒ 实现层在完成时会把 `progress` 写成 **`1f`**（见 `ProgressStateEffect` 的 KDoc）
 * ⇒ ⇒ 于是 `< 1f` 在完成那一刻变 false ⇒ **进度条消失** ✅
 * ⇒ ⇒ 若上界写成 `>= 1f` 或干脆不带上界 ⇒ **进度条卡在满格、永不消失**
 * ⇒ ⇒ 那正是 T2.8 要消灭的那个坏表现。
 *
 * ⚠️ **下界 `> 0f` 的意义**：desktop / js / wasmJs 上库的 `onProgressChanged`
 * **只回调、不写 state** ⇒ 那些平台上进度**恒为 0**
 * ⇒ ⇒ 下界让它们**不显示**（满足验收标准「要么消失、要么显示真实进度」）。
 * ⇒ ⇒ 而 Android / iOS 上刚开始那一帧也是 0 ⇒ 那一帧不显示、之后才显示 ⇒ **不闪**。
 *
 * ⚠️ **为什么不用 `isLoading` 参与判断**：
 * 库的 `isLoading` = `loadingState !is LoadingState.Finished`
 * ⇒ **`Failed` / `Cancelled` 也算 loading** ⇒ 失败后可能一直转。
 */
fun shouldShowProgressBar(progress: Float): Boolean = progress > 0f && progress < 1f

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
        //
        // ⚠️⚠️ **原来的条件恒真**（T2.8）：`state.isLoading || state.progress < 1f`
        // ⇒ 而 `updateProgress(...)` **全仓调用点数为 0** ⇒ `progress` **恒为 0**
        // ⇒ ⇒ `progress < 1f` **恒真** ⇒ 进度条**永远停在 0%、加载完也不消失**。
        //
        // ⇒ 判据见 [shouldShowProgressBar] 的 KDoc（单独抽出来是为了能直接测它）。
        if (shouldShowProgressBar(state.progress)) {
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
