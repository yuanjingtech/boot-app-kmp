package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.yuanjingtech.boot.app.kmp.webview.WebViewError
import com.yuanjingtech.boot.app.kmp.webview.WebViewState

/**
 * 加载失败的来源。
 *
 * 抽出这一层是为了让"把加载失败同步进 [WebViewState]"这件事**可测**：
 * `compose-webview` 的错误类型 `com.parkwoocheol.composewebview.WebViewError`
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
 * 库的 `onWebError` 是**回调**（不是响应式状态）⇒ 错误是"之后"才到的
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
 * 库的 `ComposeWebView` 有 `errorContent: @Composable (List<WebViewError>) -> Unit` 参数
 * ⇒ 加载失败时库会把错误**递出来**
 * ⇒ 但 boot 的 `WebViewState.error` **永远是 null**
 * ⇒ 消费方（KMP 端"缓存条目加载失败后从列表移除"）**拿不到失败信号**。
 *
 * ## 为什么用 snapshotFlow
 *
 * 错误是**回调**送进来的（`errorContent` 是 composable slot，不是响应式状态）
 * ⇒ 把它写进 `MutableState` ⇒ 然后用 [snapshotFlow] 在快照作用域里读
 * ⇒ 状态一变就能收到通知。
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
 * 这是库的 `errorContent` 回调的落点：
 * 库的错误类型**不能**透出到 boot 的 `webview` 模块 ⇒ 在这里映射成 [LoadError]。
 *
 * ⚠️ 只保留**最后一条**：库的 `errorContent` 给的是"这次请求的所有错误"，
 * 而 boot 的 `WebViewState.error` 是单值 ⇒ 取最后一条即可。
 */
internal fun applyLoadErrors(
    source: MutableLoadErrorSource,
    errors: List<com.parkwoocheol.composewebview.WebViewError>,
) {
    val last = errors.lastOrNull()
    source.lastError = last?.let { LoadError(it.errorCode, it.description) }
}
