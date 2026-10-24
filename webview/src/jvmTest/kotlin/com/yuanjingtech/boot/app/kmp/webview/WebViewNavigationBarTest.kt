package com.yuanjingtech.boot.app.kmp.webview

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * [WebViewNavigationBar] 的行为测试 —— T2.7。
 *
 * ## 这个工具栏要满足什么（RN 端 `SubAppWebView.tsx` 的底部工具栏）
 *
 * RN 端有两个 `Button`：`title={"back"}` → `webViewElement.current.goBack()`、
 * `title={"forward"}` → `goForward()`。
 *
 * ⚠️ **RN 的按钮是「假按钮」** —— 它的 enabled 状态**跟真实 `canGoBack` 无关**
 * ⇒ ⇒ 按「补全而非等价还原」的口径，**KMP 端不做假按钮**
 * ⇒ ⇒ **enabled 必须跟真实导航状态走**（`tasks.md` T2.7 验收第 ① 条）。
 *
 * ## ⚠️ 这里最容易踩的一个坑：**enabled 不能写死**
 *
 * 假想的坏实现：
 * ```kotlin
 * TextButton(onClick = { state.controller?.goBack() }, enabled = true) { ... }
 * ```
 * ⇒ 按钮**永远可点** ⇒ 用户在没有历史时点「后退」⇒ 点了没反应
 * ⇒ ⇒ 这是 T2.7 验收第 ① 条明令禁止的「假按钮」。
 *
 * ⇒ 所以下面每一条 enabled 断言都**同时**验两个方向：
 * **真值为 false 时必须 disabled**、**真值为 true 时必须 enabled**
 * ⇒ 单向断言（只验 disabled）会漏掉「写死 true」这个 bug。
 *
 * ## ⚠️ 第二个坑：**点击必须真的调 controller**
 *
 * 光断言 enabled 不够 ⇒ 还要断言 **点击真的调到了 `goBack()` / `goForward()`**
 * ⇒ 而且要断言 **disabled 时点击无效**（否则「点不动」这条就没有鉴别力）。
 *
 * ## 第三个坑：**controller 是后挂的 ⇒ 按钮必须能跟着变**
 *
 * 挂载发生在内层组合里，读 `controller` 的是外层工具栏
 * ⇒ ⇒ `controller` 必须是可观察状态 ⇒ **挂上后外层要重组**、按钮要跟着变
 * ⇒ ⇒ 否则按钮**永远 disabled**（一个永远点不动的假按钮）。
 * ⇒ 这条由 [controllerBecomesUsableAfterAttach] 钉住。
 */
@OptIn(ExperimentalTestApi::class)
class WebViewNavigationBarTest {

    /** 假的 controller：只记录调用次数，用来证明「真的调到了 controller」。 */
    private class FakeWebViewController : WebViewController {
        var goBackCalls = 0
        var goForwardCalls = 0

        override fun loadUrl(url: String) = Unit
        override fun reload() = Unit
        override fun goBack() { goBackCalls++ }
        override fun goForward() { goForwardCalls++ }
        override fun stopLoading() = Unit
        override fun clearCache() = Unit
    }

    @Test
    fun showsBackAndForwardLabelsInChinese() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        // ⚠️ RN 那两个 title={"back"} / {"forward"} 经 material-ui 的 upperCase 渲染成
        // "BACK" / "FORWARD" ⇒ 那是**没翻译的占位符** ⇒ 按「补全而非等价还原」不照抄
        onNodeWithText("后退").assertTextEquals("后退")
        onNodeWithText("前进").assertTextEquals("前进")
    }

    @Test
    fun backButtonIsDisabledWhenCannotGoBack() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        // 前提断言：真值确实是 false（否则下面那条断言没有鉴别力）
        assertFalse(state.canGoBack, "前提：初始状态没有历史 ⇒ canGoBack 必须是 false")

        onNodeWithText("后退").assertIsNotEnabled()
    }

    @Test
    fun forwardButtonIsDisabledWhenCannotGoForward() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        assertFalse(state.canGoForward, "前提：初始状态没有前进历史 ⇒ canGoForward 必须是 false")

        onNodeWithText("前进").assertIsNotEnabled()
    }

    @Test
    fun backButtonIsEnabledWhenCanGoBack() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")
        state.updateNavigation(canGoBack = true, canGoForward = false)

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        assertTrue(state.canGoBack, "前提：真值已是 true")
        onNodeWithText("后退").assertIsEnabled()
    }

    @Test
    fun forwardButtonIsEnabledWhenCanGoForward() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")
        state.updateNavigation(canGoBack = false, canGoForward = true)

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        assertTrue(state.canGoForward, "前提：真值已是 true")
        onNodeWithText("前进").assertIsEnabled()
    }

    @Test
    fun clickingBackCallsControllerGoBack() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")
        val controller = FakeWebViewController()
        state.updateNavigation(canGoBack = true, canGoForward = false)
        state.attachController(controller)

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        assertEquals(0, controller.goBackCalls, "前提：还没点过")

        onNodeWithText("后退").performClick()
        waitForIdle()

        assertEquals(1, controller.goBackCalls, "点「后退」必须真的调 controller.goBack()")
    }

    @Test
    fun clickingForwardCallsControllerGoForward() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")
        val controller = FakeWebViewController()
        state.updateNavigation(canGoBack = false, canGoForward = true)
        state.attachController(controller)

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        assertEquals(0, controller.goForwardCalls, "前提：还没点过")

        onNodeWithText("前进").performClick()
        waitForIdle()

        assertEquals(1, controller.goForwardCalls, "点「前进」必须真的调 controller.goForward()")
    }

    @Test
    fun clickingDisabledButtonDoesNothing() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")
        val controller = FakeWebViewController()
        state.updateNavigation(canGoBack = false, canGoForward = false)
        state.attachController(controller)

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        onNodeWithText("后退").performClick()
        onNodeWithText("前进").performClick()
        waitForIdle()

        assertEquals(0, controller.goBackCalls, "disabled 的「后退」不该调 controller")
        assertEquals(0, controller.goForwardCalls, "disabled 的「前进」不该调 controller")
    }

    @Test
    fun navigationStateChangeUpdatesButtonEnabled() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")
        state.updateNavigation(canGoBack = false, canGoForward = false)

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        onNodeWithText("后退").assertIsNotEnabled()

        // 真值变了 ⇒ 按钮必须跟着变（这要求工具栏**读的是可观察状态**）
        state.updateNavigation(canGoBack = true, canGoForward = true)
        waitForIdle()

        onNodeWithText("后退").assertIsEnabled()
        onNodeWithText("前进").assertIsEnabled()
    }

    @Test
    fun controllerBecomesUsableAfterAttach() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")
        val controller = FakeWebViewController()
        state.updateNavigation(canGoBack = true, canGoForward = true)

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        // 还没挂 controller ⇒ 点了也不该有效果（没有可调的对象）
        onNodeWithText("后退").performClick()
        waitForIdle()
        assertEquals(0, controller.goBackCalls, "还没挂 controller 时不该有调用")

        // ⚠️ 关键：controller 是**后挂**的 ⇒ 挂上之后按钮必须真的能用
        // ⇒ 这要求 `controller` 是**可观察状态**（mutableStateOf）
        // ⇒ 若实现用的是普通 var ⇒ 工具栏不会重组 ⇒ 按钮永远点不动 ⇒ 这条会红
        state.attachController(controller)
        waitForIdle()

        onNodeWithText("后退").performClick()
        waitForIdle()

        assertEquals(1, controller.goBackCalls, "挂上 controller 后点「后退」必须真的调得动")
    }

    @Test
    fun detachingControllerStopsForwardingClicks() = runComposeUiTest {
        val state = WebViewState(initialUrl = "https://example.com")
        val controller = FakeWebViewController()
        state.updateNavigation(canGoBack = true, canGoForward = true)
        state.attachController(controller)

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        onNodeWithText("后退").performClick()
        waitForIdle()
        assertEquals(1, controller.goBackCalls, "前提：挂上时点一次是有效的")

        state.detachController()
        waitForIdle()

        onNodeWithText("后退").performClick()
        waitForIdle()

        assertEquals(1, controller.goBackCalls, "controller 摘掉后不该再调到它")
    }

    @Test
    fun enabledStateFollowsNavigationStateNotAConstant() = runComposeUiTest {
        // ⚠️ 这条是「写死 true」的专杀用例：
        // 把 enabled 写死成 true ⇒ 下面「初始（真值为 false）时必须 disabled」那条会红
        // ⇒ ⇒ 单测 enabled=true 的那一侧**根本抓不到**这个 bug
        // ⇒ ⇒ 必须两种真值都测一遍。
        val state = WebViewState(initialUrl = "https://example.com")

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        assertFalse(state.canGoBack)
        assertFalse(state.canGoForward)

        onNodeWithText("后退").assertIsNotEnabled()
        onNodeWithText("前进").assertIsNotEnabled()
    }

    @Test
    fun buttonEnabledStateIsIndependentPerDirection() = runComposeUiTest {
        // 只有后退历史、没有前进历史 ⇒ 两个按钮必须**分别**跟随各自的状态
        // ⇒ ⇒ 若两个按钮共用一个 enabled（例如都跟 canGoBack 走）⇒ 这条会红
        val state = WebViewState(initialUrl = "https://example.com")
        state.updateNavigation(canGoBack = true, canGoForward = false)

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        onNodeWithText("后退").assertIsEnabled()
        onNodeWithText("前进").assertIsNotEnabled()
    }

    @Test
    fun navigationStateIsReadFromStateHolderNotFromController() = runComposeUiTest {
        // ⚠️ 真值来源只能是 `state.canGoBack` / `state.canGoForward`
        // （那是 boot `NavigationStateEffect` 从库的 controller 转发过来的**滞后副本**）
        // ⇒ 若实现改成直接问 controller（那要拿到库的类型 ⇒ 依赖面扩大），
        //    在 `webview` 这个模块里根本编译不过 ⇒ 这条用例的作用是钉住"来源是 state holder"
        val state = WebViewState(initialUrl = "https://example.com")
        state.updateNavigation(canGoBack = true, canGoForward = true)
        state.attachController(FakeWebViewController())

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        onNodeWithText("后退").assertIsEnabled()
        onNodeWithText("前进").assertIsEnabled()
    }

    @Test
    fun stateHolderCanGoBackIsTheOnlySourceOfTruth() = runComposeUiTest {
        // ⚠️ 「滞后副本」这一条口径的落点：
        // state holder 的 canGoBack 是**滞后副本** ⇒ 工具栏必须**照抄它**
        // ⇒ ⇒ 「enabled 不会比真值更早为 true」这条验收口径，
        //    在工具栏这一层的落点就是：**只读 state holder，不自己猜、不缓存**
        //
        // ⚠️ 鉴别力来自哪：
        // ① 若实现把 `state.canGoBack` **缓存进 remember 不带 key** ⇒
        //    后面 state holder 变了按钮不变 ⇒ 这条会红
        // ② 若实现去问 controller（boot 的 `WebViewController` 接口**根本没暴露**历史状态，
        //    所以它只能问 state holder）⇒ 编译层面就已经被钉死
        val state = WebViewState(initialUrl = "https://example.com")

        // 这个 controller 的 `goBack()` 会把 state holder 的历史状态改掉 ——
        // 模拟真实 controller：后退一次之后就没有更早的历史了
        state.attachController(object : WebViewController {
            override fun loadUrl(url: String) = Unit
            override fun reload() = Unit
            override fun goBack() { state.updateNavigation(canGoBack = false, canGoForward = false) }
            override fun goForward() = Unit
            override fun stopLoading() = Unit
            override fun clearCache() = Unit
        })

        setContent { WebViewNavigationBar(state) }
        waitForIdle()

        // 前提：state holder 侧没有历史 ⇒ 按钮必须 disabled
        assertFalse(state.canGoBack, "前提：state holder 侧没有历史")
        onNodeWithText("后退").assertIsNotEnabled()

        // 只把 **state holder** 改成 true ⇒ 工具栏必须跟着变
        state.updateNavigation(canGoBack = true, canGoForward = false)
        waitForIdle()
        onNodeWithText("后退").assertIsEnabled()

        // 点「后退」⇒ controller 把 state holder 的历史清掉 ⇒ 按钮必须**重新**变回 disabled
        // ⇒ ⇒ 这一步钉的是「每次重组都重新读 state holder」，不是「读一次就缓存住」
        onNodeWithText("后退").performClick()
        waitForIdle()

        assertFalse(state.canGoBack, "前提：点完后退，state holder 侧应该没有历史了")
        onNodeWithText("后退").assertIsNotEnabled()
    }
}
