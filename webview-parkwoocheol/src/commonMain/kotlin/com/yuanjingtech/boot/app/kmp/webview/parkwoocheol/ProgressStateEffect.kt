package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.parkwoocheol.composewebview.LoadingState
import com.yuanjingtech.boot.app.kmp.webview.WebViewState
import com.parkwoocheol.composewebview.WebViewState as LibraryWebViewState

/**
 * 加载进度的来源。
 *
 * ## 为什么抽象出这一层
 *
 * 与 [NavigationStateSource] 同理：让"把进度同步进 [WebViewState]"这件事**可测**。
 *
 * ⚠️ **库没有独立的 `progress` 字段** ⇒ 进度**藏在 `loadingState` 里**
 * （`LoadingState.Loading(progress: Float)`）。
 * ⇒ 而 `loadingState` 的 **setter 是 `internal` 的**
 * ⇒ ⇒ **测试无法直接把它设成 `Loading(0.42f)`** ⇒ ⇒ 只能靠"读"的抽象来测。
 *
 * ## ⚠️ 各平台能读到什么（按 `compose-webview` 1.8.2 的源码逐个平台核过）
 *
 * | 平台 | 库的进度写入方 | 读到的值 |
 * |---|---|---|
 * | Android | `ComposeWebChromeClient.onProgressChanged` 写 `LoadingState.Loading(newProgress / 100f)` | **0~1 的真进度** |
 * | iOS | `ComposeWebView.ios.kt` 轮询 `webView.estimatedProgress` 写 `LoadingState.Loading(progress)` | **0~1 的真进度** |
 * | desktop | `onProgressChanged` **只回调、不写 state**；只有 `onPageStarted` 写 `Loading(0f)` | **恒 0**（取不到真值） |
 * | js / wasmJs | 同 desktop | **恒 0** |
 *
 * ⇒ ⇒ **进度只能在 Android / iOS 上取到真值**；其它平台**取不到**
 * ⇒ ⇒ 那些平台上进度条**不显示**（这正是 T2.8 验收标准里的「要么消失」）。
 *
 * ## ⚠️ 为什么不能用 `isLoading` 顶替进度
 *
 * 库的 `isLoading` 实现是 `loadingState !is LoadingState.Finished`
 * ⇒ ⇒ **`Failed` / `Cancelled` 也算 loading** ⇒ 失败后可能**一直转**。
 */
internal interface ProgressSource {
    /**
     * 当前进度，**单位 0.0~1.0**；没有真值时为 `0f`。
     *
     * ⚠️ **不要用 `isLoading` 顶替** —— 见本接口的 KDoc。
     */
    val progress: Float
}

/**
 * 把库的 `loadingState` 翻译成一个**进度百分比**。
 *
 * ## 契约（这三条被测试钉死）
 *
 * | `loadingState` | 返回 | 为什么 |
 * |---|---|---|
 * | `Loading(p)` | **`p`**（0~1） | 这一支才带百分比 |
 * | `Finished` | **`1f`** | ⚠️ **不是 0f** —— 见下 |
 * | `Initializing` / `Failed` / `Cancelled` | **`0f`** | 没有百分比 |
 *
 * ## ⚠️⚠️ **`Finished` 为什么必须读成 `1f` 而不是 `0f`**
 *
 * 库的 Android / iOS 实现**在进度到 100 时写 `Finished`，而不是 `Loading(1f)`**：
 * ```kotlin
 * // Android
 * state?.loadingState = if (newProgress == 100) LoadingState.Finished
 *                         else LoadingState.Loading(newProgress / 100f)
 * // iOS
 * state.loadingState = if (progressInt >= 100) LoadingState.Finished
 *                      else LoadingState.Loading(progress.toFloat())
 * ```
 * ⇒ ⇒ **完成那一刻 `loadingState` 已经不是 `Loading` 了** ⇒ ⇒ `Loading.progress` **取不到**
 * ⇒ ⇒ 若按"不是 `Loading` 就 0f"处理 ⇒ **进度会从 0.99 掉回 0**
 * ⇒ ⇒ 后果：`webview` 模块的判据 `0f < progress < 1f` **又变 false**
 * ⇒ ⇒ ⇒ **进度条在 99% 卡住不消失** ⇒ 那正是 T2.8 要消灭的坏表现。
 *
 * ⇒ ⇒ 所以：**读到 `Finished` 时给 `1f`**（表示"到头了"）
 * ⇒ ⇒ 判据 `0f < progress < 1f` 在完成时变 false ⇒ **条会消失** ✅
 *
 * ⚠️ **顺带一个事实**：`Loading.progress` **永远不会等于 1.0**
 * ⇒ （库在 100 时就切走了 ⇒ 最大只到 `0.99`）
 * ⇒ ⇒ 所以 `< 1f` 这个上界**只在"完成"那一刻才成立** ⇒ ⇒ 它就是"让条消失"的开关。
 */
internal fun progressOf(loadingState: LoadingState): Float = when (loadingState) {
    is LoadingState.Loading -> loadingState.progress
    is LoadingState.Finished -> 1f
    // Initializing / Failed / Cancelled ⇒ 没有百分比 ⇒ 0f
    else -> 0f
}

/**
 * 从库的 [LibraryWebViewState] 里读进度。
 *
 * ## ⚠️ 为什么不直接读 `libraryState.loadingState`
 *
 * ⚠️ **`loadingState` 的 setter 是 `internal`** ⇒ **测试写不了**
 * ⇒ ⇒ 这里把"读"包一层，测试则喂一个自己控制的 [ProgressSource]。
 * ⇒ ⇒ 而"怎么把 `loadingState` 翻成百分比"抽成了 [progressOf]
 * ⇒ ⇒ **那部分是可以直接测的**（不需要造库的 state）。
 */
internal class LibraryProgressSource(
    private val libraryState: LibraryWebViewState,
) : ProgressSource {
    override val progress: Float
        get() = progressOf(libraryState.loadingState)
}

/**
 * 把加载进度同步进 [WebViewState]。
 *
 * ## 为什么必须做这件事（T2.8）
 *
 * `WebViewState.updateProgress(...)` 这个方法**早就有**，但全仓**调用点数为 0**
 * ⇒ `WebViewState.progress` **恒为 0**。
 *
 * ⇒ 而 `webview` 模块的进度条条件是 `if (state.isLoading || state.progress < 1f)`
 * ⇒ ⇒ 条件**恒真** ⇒ ⇒ 进度条**永远停在 0%、而且加载完也不消失**。
 *
 * ## 为什么用 snapshotFlow
 *
 * 来源的 `progress` 背后是 Compose 的 `MutableState`
 * （库的 `ComposeWebChromeClient` / iOS 的进度轮询会写 `loadingState`）。
 * ⇒ 必须在**快照作用域**里读，才能在它变化时收到通知 ⇒ [snapshotFlow] 正是干这个的。
 *
 * ⚠️ **读的时候要读 `progress` 这个具体值，不要读整个 `loadingState`**
 * ⇒ 因为 `loadingState` 是 sealed class，`Loading` 与 `Finished` 是**不同的对象**
 * ⇒ ⇒ 读整个对象的话，"从 Loading(0.42) 变成 Loading(0.43)"这种**同类型不同值**的变化
 * ⇒ ⇒ **`equals` 判定不等** ⇒ 反而能触发；反过来 "Loading(0.42) → Finished" 也能触发。
 * ⇒ ⇒ 两种都触发，但**读具体值更直接、也更容易断言**（`snapshotFlow` 自带去重）。
 */
@Composable
internal fun ProgressStateEffect(
    source: ProgressSource,
    stateHolder: WebViewState,
) {
    LaunchedEffect(source) {
        snapshotFlow { source.progress }.collect { progress ->
            stateHolder.updateProgress(progress)
        }
    }
}
