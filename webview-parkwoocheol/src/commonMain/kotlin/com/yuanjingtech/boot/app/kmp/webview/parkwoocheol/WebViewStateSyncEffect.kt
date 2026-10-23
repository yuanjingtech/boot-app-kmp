package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.yuanjingtech.boot.app.kmp.webview.WebViewState
import com.parkwoocheol.composewebview.WebViewState as LibraryWebViewState

/**
 * 把库的 [LibraryWebViewState] 同步进 boot 的 [WebViewState]。
 *
 * ## ⚠️ 为什么必须拆成**两个** `LaunchedEffect`
 *
 * `snapshotFlow { ... }.collect { ... }` **永不返回**（它是一条无限流）
 * ⇒ ⇒ 写在**同一个** `LaunchedEffect` 里的第二个 `collect` **永远不会被执行**
 * ⇒ ⇒ 只有第一个状态被同步，另一个状态**永远收不到**
 *
 * ⇒ 这个缺陷在 2026-10-24 被 @远景react native开发 核出来
 * ⇒ 实际症状：`WebViewState.pageTitle` **恒为 null** ⇒ KMP 侧的页面标题永远不显示。
 *
 * ⚠️ **别把它改回一个 `LaunchedEffect`** —— 编译期完全看不出来，
 * 只有测试能兜住（见 `WebViewStateSyncEffectTest`）。
 */
@Composable
internal fun WebViewStateSyncEffect(
    libraryState: LibraryWebViewState,
    stateHolder: WebViewState,
) {
    // 加载状态
    LaunchedEffect(libraryState) {
        snapshotFlow { libraryState.isLoading }.collect { isLoading ->
            stateHolder.updateLoading(isLoading)
        }
    }

    // 页面标题
    LaunchedEffect(libraryState) {
        snapshotFlow { libraryState.pageTitle }.collect { title ->
            stateHolder.updatePageTitle(title)
        }
    }
}
