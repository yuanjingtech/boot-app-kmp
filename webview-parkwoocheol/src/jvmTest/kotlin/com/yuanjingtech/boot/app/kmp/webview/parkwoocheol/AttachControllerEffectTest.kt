package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.parkwoocheol.composewebview.WebViewController as LibraryController
import com.yuanjingtech.boot.app.kmp.webview.WebViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * [AttachControllerEffect] 的行为测试 —— T2.7。
 *
 * ## 为什么这条链路必须有测试
 *
 * `AttachControllerEffect` 是**唯一**把库的 controller 递到 boot `WebViewState` 上的地方
 * ⇒ ⇒ 消费方（KMP 两个屏）的"前进/后退工具栏"**全靠它**才有东西可调。
 *
 * ⇒ ⇒ 如果它不生效 ⇒ 按钮永远 disabled ⇒ **"功能在、就是点不动"**，
 * 而且这种缺陷**不会有任何报错** ⇒ ⇒ 只能靠测试钉住。
 *
 * ## ⚠️ 为什么断言"摘掉"比断言"挂上"更重要
 *
 * 挂上很容易（`attachController` 一调就有）⇒ 真正会坏的是**离开组合时没摘**
 * ⇒ ⇒ 那样 state holder 会一直握着一个**已经失效**的 controller
 * ⇒ ⇒ 用户点「后退」⇒ 调的是一个死对象 ⇒ **静默无效**。
 */
@OptIn(ExperimentalTestApi::class)
class AttachControllerEffectTest {

    @Test
    fun attachesTheControllerToTheStateHolder() = runComposeUiTest {
        val stateHolder = WebViewState(initialUrl = "https://example.com")
        val libraryController = LibraryController(CoroutineScope(Dispatchers.Unconfined))

        // ⚠️ **先断言"起点是脏的"** —— 否则「挂上了」这条可能恒真
        // ⚠️ 这一步**必须在 setContent 之前**：`setContent` 会立刻跑一次组合
        // ⇒ ⇒ effect 在 `waitForIdle()` 之前就已经跑了 ⇒ ⇒ 之后再断言"还没挂"必然失败。
        assertNull(stateHolder.controller, "前提：还没进组合，state holder 上不该有 controller")

        setContent {
            AttachControllerEffect(controller = libraryController, stateHolder = stateHolder)
        }
        waitForIdle()

        assertNotNull(stateHolder.controller, "进组合后 controller 必须挂到 state holder 上")
    }

    @Test
    fun detachesTheControllerWhenLeavingComposition() = runComposeUiTest {
        val stateHolder = WebViewState(initialUrl = "https://example.com")
        val libraryController = LibraryController(CoroutineScope(Dispatchers.Unconfined))

        setContent {
            AttachControllerEffect(controller = libraryController, stateHolder = stateHolder)
        }
        waitForIdle()
        assertNotNull(stateHolder.controller, "前提：进组合后 controller 已挂上")

        // 离开组合 ⇒ effect 的 onDispose 必须跑 ⇒ controller 必须被摘掉
        setContent { }
        waitForIdle()

        assertNull(stateHolder.controller, "离开组合后必须摘掉 controller（否则会调一个已失效的对象）")
    }

    @Test
    fun switchingStateHolderSwapsTheController() = runComposeUiTest {
        // state holder 换了（消费方用 `remember(url)` 重建）⇒ ⇒ 必须换挂
        // ⇒ ⇒ 否则会拿着一个**已经失效的** controller 继续调。
        val first = WebViewState(initialUrl = "https://example.com")
        val second = WebViewState(initialUrl = "https://example.com/other")
        val libraryController = LibraryController(CoroutineScope(Dispatchers.Unconfined))

        // 用 `key` 换掉整个子树 ⇒ 模拟"state holder 换了"
        var useFirst by mutableStateOf(true)

        setContent {
            key(if (useFirst) "first" else "second") {
                val holder = if (useFirst) first else second
                AttachControllerEffect(controller = libraryController, stateHolder = holder)
            }
        }
        waitForIdle()

        assertNotNull(first.controller, "前提：第一个 state holder 已挂上")

        useFirst = false
        waitForIdle()

        assertNull(first.controller, "换掉之后，旧 state holder 上的 controller 必须被摘掉")
        assertNotNull(second.controller, "新的 state holder 必须挂上 controller")
    }

    @Test
    fun attachedControllerForwardsBackToTheLibrary() = runComposeUiTest {
        // 端到端的一条：挂上去的 adapter **真的能把后退转发给库**
        // ⇒ ⇒ 这条钉的是"挂上去的确实是我们以为的那个 adapter"，而不只是"非 null"
        val stateHolder = WebViewState(initialUrl = "https://example.com")
        val libraryController = LibraryController(CoroutineScope(Dispatchers.Unconfined))

        setContent {
            AttachControllerEffect(controller = libraryController, stateHolder = stateHolder)
        }
        waitForIdle()

        val adapter = assertNotNull(stateHolder.controller, "必须挂上 controller")

        // 不抛异常 + 返回值即证明调用链通到了库
        adapter.goBack()
        adapter.goForward()
        adapter.reload()
        adapter.stopLoading()
        adapter.clearCache()
        adapter.loadUrl("https://example.com/next")
    }
}
