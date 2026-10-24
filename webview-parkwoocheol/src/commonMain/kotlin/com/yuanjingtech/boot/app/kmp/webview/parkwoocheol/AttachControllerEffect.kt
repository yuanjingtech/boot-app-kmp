package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.parkwoocheol.composewebview.WebViewController as LibraryController
import com.yuanjingtech.boot.app.kmp.webview.WebViewState

/**
 * **把库的 controller 挂到 boot 的 [WebViewState] 上**（T2.7）。
 *
 * ## 为什么必须挂
 *
 * boot 的 `WebViewState` 在 T2.7 之前**没有任何途径**发起前进/后退
 * ⇒ ⇒ `WebViewController` 接口是个**悬空契约**（零实现方、零引用方）
 * ⇒ ⇒ 消费方（KMP 的两个子应用屏）的"前进/后退工具栏"点了也没东西可调
 * ⇒ ⇒ 必须把 controller 递上去。
 *
 * ## ⚠️ 为什么用 `DisposableEffect` 而不是 `LaunchedEffect`
 *
 * `LaunchedEffect` 的 `onDispose` 在**协程结束**时才跑
 * ⇒ ⇒ 而这里要表达的是"这个组合还在不在"
 * ⇒ ⇒ `DisposableEffect` 才是对的。
 *
 * ## ⚠️ 为什么 `stateHolder` 要进 key
 *
 * state holder 换了（消费方用 `remember(url)` 重建）⇒ ⇒ 必须把旧的摘掉、把新的挂上
 * ⇒ ⇒ 否则会拿着一个**已经失效的** controller 继续调。
 *
 * ⚠️ **顺序很重要**：`DisposableEffect` 的 `onDispose` 是在**旧 key 的 effect 被拆掉**时跑
 * ⇒ ⇒ 如果 state holder 换了 ⇒ 先跑旧 effect 的 `onDispose`（摘掉）⇒ 再跑新 effect（挂上）
 * ⇒ ⇒ 顺序天然正确。
 */
@Composable
internal fun AttachControllerEffect(
    controller: LibraryController,
    stateHolder: WebViewState,
) {
    DisposableEffect(controller, stateHolder) {
        val adapter = LibraryWebViewController(controller)
        stateHolder.attachController(adapter)
        onDispose {
            stateHolder.detachController()
        }
    }
}
