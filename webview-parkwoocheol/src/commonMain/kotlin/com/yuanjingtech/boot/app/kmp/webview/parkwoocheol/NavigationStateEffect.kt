package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.yuanjingtech.boot.app.kmp.webview.WebViewState

/**
 * 导航状态（能否前进/后退）的来源。
 *
 * 抽象出这一层是为了让"把导航状态同步进 [WebViewState]"这件事**可测**：
 * `compose-webview` 的 [com.parkwoocheol.composewebview.WebViewController.canGoBack]
 * 背后是 Compose 的 `MutableState`，但它的 setter 在库内部是 `internal` 的
 * （字节码里是 `setCanGoBack$compose_webview`）⇒ **外部无法在测试里把它设成 true**。
 *
 * ⇒ 所以这里只依赖"能不能读"，测试则喂一个自己控制的可变来源。
 */
internal interface NavigationStateSource {
    /** 是否有历史可以后退 */
    val canGoBack: Boolean

    /** 是否有历史可以前进 */
    val canGoForward: Boolean
}

/**
 * 把导航状态同步进 [WebViewState]。
 *
 * ## 为什么必须做这件事
 *
 * `WebViewState.updateNavigation(canGoBack, canGoForward)` 这个方法**早就有**，
 * 但全仓**调用点数为 0** ⇒ `WebViewState.canGoBack` / `canGoForward` **恒为 false**。
 *
 * ⇒ 后果：消费方（KMP 端的返回键拦截、前进/后退工具栏）拿到的永远是 `false`
 * ⇒ 返回键拦截**永远不启用**、前进后退按钮**永远 disabled**。
 *
 * ## 为什么用 snapshotFlow
 *
 * 来源（controller）的 `canGoBack` 背后是 Compose 的 `MutableState`
 * （库的默认 `ComposeWebViewClient` 在 `onPageStarted` / `onPageFinished` 里会写它）。
 * ⇒ 必须在**快照作用域**里读，才能在它变化时收到通知 ⇒ [snapshotFlow] 正是干这个的。
 *
 * ⚠️ 来源对象必须是**稳定的**（用 `remember` 固定住），否则 `LaunchedEffect(source)`
 * 的 key 每次重组都变 ⇒ 收集协程被反复重启 ⇒ 状态同步会断断续续。
 */
@Composable
internal fun NavigationStateEffect(
    source: NavigationStateSource,
    stateHolder: WebViewState,
) {
    LaunchedEffect(source) {
        snapshotFlow { source.canGoBack to source.canGoForward }.collect { (back, forward) ->
            stateHolder.updateNavigation(back, forward)
        }
    }
}
