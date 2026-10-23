package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.parkwoocheol.composewebview.PlatformBitmap
import com.parkwoocheol.composewebview.WebView
import com.parkwoocheol.composewebview.client.ComposeWebViewClient
import com.yuanjingtech.boot.app.kmp.webview.WebViewError
import com.yuanjingtech.boot.app.kmp.webview.WebViewState

/**
 * 加载失败的来源。
 *
 * 抽出这一层是为了让"把加载失败同步进 [WebViewState]"这件事**可测**：
 * 库的 `com.parkwoocheol.composewebview.WebViewError`
 * 是**库的**类型 ⇒ boot 的 `webview` 模块（`WebViewState` 所在处）**没有**依赖
 * `compose-webview` ⇒ **不能把库的类型透出去**。
 *
 * ⇒ 所以这里只暴露"错误码 + 描述"，由 boot 侧自己映射成 [WebViewError]。
 */
internal interface LoadErrorSource {
    /** 最近一次加载失败（没有失败时为 null） */
    val lastError: LoadError?
}

/** boot 自己定义的加载失败信息（不暴露库的 `WebViewError` 类型）。 */
internal data class LoadError(
    /** 错误码（库的 `errorCode`） */
    val code: Int,
    /** 错误描述（库的 `description`） */
    val description: String,
)

/**
 * [LoadErrorSource] 的可变实现。
 *
 * ⚠️ `lastError` 必须是 **Compose 的 `MutableState`**：
 * 库的错误是**回调**送进来的（不是响应式状态）⇒ 错误是"之后"才到的
 * ⇒ 而 [LoadErrorEffect] 里的 `LaunchedEffect` 只在 key 变化时重启
 * ⇒ ⇒ 错误到了之后，必须靠 **状态变化**把 effect 唤醒 ⇒ 所以这里用 `mutableStateOf`。
 */
internal class MutableLoadErrorSource : LoadErrorSource {
    override var lastError: LoadError? by mutableStateOf(null)
}

/**
 * 把加载失败同步进 [WebViewState]。
 *
 * ## 为什么需要这件事
 *
 * 库的 `ComposeWebView` 有 `errorContent: @Composable (List<WebViewError>) -> Unit` 这个
 * **composable slot**（⚠️ **不是** `onWebError`，也不是普通回调）
 * ⇒ 加载失败时库会把错误**递出来**
 * ⇒ 但 boot 的 `WebViewState.error` **永远是 null**
 * ⇒ 消费方（KMP 端"缓存条目加载失败后从列表移除"）**拿不到失败信号**。
 *
 * ## 为什么用 snapshotFlow
 *
 * 错误是**在组合过程中**被 slot 递进来的（`errorContent` 是 **composable slot**，
 * 不是响应式状态 ⇒ 不是"回调"，也不能像 `LaunchedEffect` 那样订阅）
 * ⇒ 把它写进 `MutableState` ⇒ 然后用 [snapshotFlow] 在快照作用域里读
 * ⇒ 状态一变就能收到通知。
 *
 * ## ⚠️ 这里**只负责"错误进来"，不负责"错误清掉"**
 *
 * 清空走的是 [createLoadErrorClearingClient]（覆写库的 `onPageStarted`）。
 * 原因见那个函数的说明：库的 `errorContent` slot **只在列表非空时才会被调用**
 * ⇒ ⇒ **永远递不到空列表** ⇒ 靠 slot 自己是清不掉的。
 */
@Composable
internal fun LoadErrorEffect(
    source: LoadErrorSource,
    stateHolder: WebViewState,
) {
    LaunchedEffect(source, stateHolder) {
        snapshotFlow { source.lastError }.collect { error ->
            stateHolder.updateError(error?.let { WebViewError(it.code, it.description) })
        }
    }
}

/**
 * 把库的错误列表映射成 boot 自己的错误，写进 [source]。
 *
 * 这是库的 `errorContent` slot 的落点：
 * 库的错误类型**不能**透出到 boot 的 `webview` 模块 ⇒ 在这里映射成 [LoadError]。
 *
 * ⚠️ 只保留**最后一条**：库的 `errorContent` 给的是"这次请求的所有错误"，
 * 而 boot 的 `WebViewState.error` 是单值 ⇒ 取最后一条即可。
 *
 * ⚠️ 库的 `errorContent` 是 **composable slot**（`@Composable (List<WebViewError>) -> Unit`），
 * **不是**普通回调 ⇒ 它是**在组合过程中**被调用的。
 */
internal fun applyLoadErrors(
    source: MutableLoadErrorSource,
    errors: List<com.parkwoocheol.composewebview.WebViewError>,
) {
    val last = errors.lastOrNull()
    source.lastError = last?.let { LoadError(it.errorCode, it.description) }
}

/**
 * 造一个 client：**每次开始加载新页面，就把上一次的加载失败清掉**。
 *
 * ## 为什么必须有这条清空路径（缺陷的准确形状）
 *
 * 库的 `ComposeWebView` 在 Android / iOS 上是这样调 `errorContent` 的
 * （`ComposeWebView.android.kt` / `ComposeWebView.ios.kt`）：
 * ```kotlin
 * if (state.errorsForCurrentRequest.isNotEmpty()) {
 *     errorContent(state.errorsForCurrentRequest)
 * }
 * ```
 * ⇒ ⇒ **列表为空 ⇒ `errorContent` 根本不会被调用** ⇒ ⇒ **永远递不到空列表**
 * ⇒ ⇒ 于是 boot 的 `WebViewState.error` **一旦设成非 null 就再也清不回 null**
 * ⇒ ⇒ `WebViewState.isReady`（= `!isLoading && error == null`）**永远为 false**
 * ⇒ ⇒ 消费方拿到的"加载失败"信号**粘住不放**。
 *
 * ## 为什么选"覆写库的 `onPageStarted`"
 *
 * "新一轮加载开始"这个语义**本来就由库定义**：库的 Android / iOS 实现在自己的
 * `onPageStarted` 里**主动清空** `errorsForCurrentRequest`
 * （`ComposeWebViewClient.kt`：`webViewState?.errorsForCurrentRequest?.clear()`）
 * ⇒ ⇒ 库的 client 回调就是"新一轮加载开始"的**现成信号**
 * ⇒ ⇒ 我们在同一个回调里清**我们自己的** `lastError`
 * （`super.onPageStarted` 只清**库的列表**，不会碰我们的 `lastError`）。
 *
 * ⚠️ **必须调 `super.onPageStarted`**：库自己的行为全在它里面
 * —— `loadingState` / `lastLoadedUrl` / `canGoBack` / `canGoForward` 的写回都在那
 * ⇒ ⇒ 漏掉它 ⇒ **T2.6 第 ① 条（导航状态）会一起坏掉**。
 *
 * ## 各平台的实际覆盖情况（按 `compose-webview` 1.8.2 的源码逐个平台核过）
 *
 * | 平台 | client 的 `onPageStarted` 会被调用吗 | 错误清得掉吗 |
 * |---|---|---|
 * | Android | ✅ `AndroidView { webViewClient = client }` | ✅ |
 * | iOS | ✅ `ComposeWebViewDelegate` 里 `client.onPageStarted(...)` | ✅ |
 * | desktop | ✅ JCEF 的 load handler 里 `client.onPageStarted(...)` | ✅ |
 * | js / wasmJs | ❌ **库的 client 在这两个目标上从未被调用** | ⚠️ 见下 |
 *
 * ⚠️ **js / wasmJs 上覆写不触发** ⇒ 但**那条路上本来就没有错误可清**：
 * 那两个目标的 `onReceivedError` **不往 `errorsForCurrentRequest` 写**
 * ⇒ ⇒ 列表恒空 ⇒ ⇒ **`errorContent` 也从未被调用**
 * ⇒ ⇒ 错误从产生到清空**都不会发生** ⇒ **不需要这条路径**。
 * （所以这里**不能**写成"所有平台都覆盖了"——那是假的。）
 *
 * ## 为什么**不**用另外两种思路（都验过，是死路）
 *
 * - ❌ **"URL 变化时清"**：boot 的 `WebViewState.updateUrl`（`webview` 模块）
 *   **全仓没有调用点** ⇒ 靠它清永远等不到那一拍。
 * - ❌ **"盯 `isLoading` 布尔"**：`compose-webview` **全库无人写**
 *   `LoadingState.Failed` / `Cancelled` ⇒ 失败之后 `isLoading` **仍然是 true**
 *   ⇒ ⇒ 它不会从 true 跳回 false，**看不出"又开始了新一轮加载"**。
 */
internal fun createLoadErrorClearingClient(source: MutableLoadErrorSource): ComposeWebViewClient =
    object : ComposeWebViewClient() {
        override fun onPageStarted(
            view: WebView?,
            url: String?,
            favicon: PlatformBitmap?,
        ) {
            super.onPageStarted(view, url, favicon)
            source.lastError = null
        }
    }
