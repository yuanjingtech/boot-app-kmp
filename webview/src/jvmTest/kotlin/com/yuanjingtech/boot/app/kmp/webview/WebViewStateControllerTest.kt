package com.yuanjingtech.boot.app.kmp.webview

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * [WebViewState.attachController] / [WebViewState.detachController] 的行为测试 —— T2.7。
 *
 * ## 为什么需要「挂 controller」这一步
 *
 * boot 的 `WebViewController` 接口（`loadUrl` / `reload` / `goBack` / `goForward` / …）
 * **在 T2.7 之前是一个悬空契约** ⇒ **全仓零实现方、零引用方**
 * ⇒ ⇒ 消费方（KMP 的两个子应用屏）**没有任何办法发起前进/后退**。
 *
 * ⇒ T2.7 要在 `WebViewState` 上挂一个 controller 引用 ⇒ 工具栏才有东西可调。
 *
 * ## ⚠️ 为什么 `controller` 必须是 **`mutableStateOf`**（而不是普通 `var`）
 *
 * 挂载发生在**内层**组合里（`webview-parkwoocheol` 的 `WebViewScreen`），
 * 而**读**它的是**外层**（KMP 的工具栏）。
 *
 * ⇒ 若用普通 `var` ⇒ 外层在首帧组合时读到的是 `null`，而这个 `null` **不是可观察状态**
 * ⇒ ⇒ 外层**不会因为后续挂上 controller 而重组**
 * ⇒ ⇒ **按钮永远 disabled**（一个"看起来能用、其实永远点不动"的假按钮）。
 *
 * ⇒ 所以这里必须有两条测试：
 * ① **挂上去能读到**（值对不对）
 * ② **挂上去之后重组会跟着变**（可观察性对不对）—— 见 [WebViewNavigationBarTest]
 *
 * ## 鉴别力说明
 *
 * - 把 `attachController` 里的赋值删掉 ⇒ 这些用例会红
 * - 把 `detachController` 里的清空删掉 ⇒ 「卸载后应为 null」那条会红
 * - 把 `controller` 改成普通 `var` ⇒ 「挂上后重组能读到」那条会红（见 `WebViewNavigationBarTest`）
 */
class WebViewStateControllerTest {

    /** 假的 controller：只记录调用次数，用来证明「真的调到了某个 controller」。 */
    private class FakeWebViewController : WebViewController {
        var loadUrlCalls = 0
        var reloadCalls = 0
        var goBackCalls = 0
        var goForwardCalls = 0
        var stopLoadingCalls = 0
        var clearCacheCalls = 0

        override fun loadUrl(url: String) { loadUrlCalls++ }
        override fun reload() { reloadCalls++ }
        override fun goBack() { goBackCalls++ }
        override fun goForward() { goForwardCalls++ }
        override fun stopLoading() { stopLoadingCalls++ }
        override fun clearCache() { clearCacheCalls++ }
    }

    @Test
    fun startsWithoutController() {
        val state = WebViewState(initialUrl = "https://example.com")

        // ⚠️ 先断言"起点是脏的"：新建的 state 必须**没有** controller
        // ⇒ 否则「detach 之后是 null」这条用例就没有鉴别力（恒真也能绿）
        assertNull(state.controller, "新建的 state 不该已经挂着 controller")
    }

    @Test
    fun attachesController() {
        val state = WebViewState(initialUrl = "https://example.com")
        val controller = FakeWebViewController()

        state.attachController(controller)

        assertNotNull(state.controller, "attachController 之后必须能读到 controller")
        assertSame(controller, state.controller, "读到的必须就是挂上去的那一个")
    }

    @Test
    fun detachesController() {
        val state = WebViewState(initialUrl = "https://example.com")
        val controller = FakeWebViewController()

        state.attachController(controller)
        assertNotNull(state.controller, "前提：先挂上，才能验「卸载后摘干净」")

        state.detachController()

        assertNull(state.controller, "detachController 之后必须摘干净 —— 否则会调到已失效的 controller")
    }

    @Test
    fun detachWithoutAttachIsHarmless() {
        val state = WebViewState(initialUrl = "https://example.com")

        // 幂等：先 detach 再 detach 不该抛异常，也不该凭空造出 controller
        state.detachController()
        state.detachController()

        assertNull(state.controller, "没挂过就 detach，controller 仍应是 null")
    }

    @Test
    fun detachingRemovesOnlyTheControllerAndKeepsOtherState() {
        val state = WebViewState(initialUrl = "https://example.com")
        state.updateNavigation(canGoBack = true, canGoForward = true)
        state.updatePageTitle("标题")

        state.attachController(FakeWebViewController())
        state.detachController()

        assertNull(state.controller, "controller 应被摘掉")
        assertTrue(state.canGoBack, "detach 只该动 controller，不该动导航状态")
        assertTrue(state.canGoForward, "detach 只该动 controller，不该动导航状态")
        assertEquals("标题", state.pageTitle, "detach 只该动 controller，不该动标题")
    }

    @Test
    fun attachingReplacesPreviousController() {
        val state = WebViewState(initialUrl = "https://example.com")
        val first = FakeWebViewController()
        val second = FakeWebViewController()

        state.attachController(first)
        state.attachController(second)

        assertSame(second, state.controller, "后挂的应覆盖先挂的（重组时 controller 可能换实例）")
    }
}
