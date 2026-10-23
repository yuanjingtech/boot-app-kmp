package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.yuanjingtech.boot.app.kmp.webview.WebViewState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * [NavigationStateEffect] 的行为测试。
 *
 * ## 要防的是什么回归
 *
 * `WebViewState.updateNavigation(canGoBack, canGoForward)` **早就有**，
 * 但全仓**调用点数为 0** ⇒ `WebViewState.canGoBack` / `canGoForward` **恒为 false**。
 *
 * ⇒ 后果：消费方拿到的永远是 `false`
 * ⇒ 返回键拦截**永远不启用**、前进后退按钮**永远 disabled**。
 *
 * ⇒ 而这个缺陷**编译期完全看不出来**（`updateNavigation` 没人调，编译照样过）
 * ⇒ ⇒ **只能靠测试兜住**。
 *
 * ## 为什么用假的来源而不是真的 controller
 *
 * `compose-webview` 的 `WebViewController.canGoBack` 背后是 Compose 的 `MutableState`，
 * 但它的 setter 在库里是 `internal` 的（字节码里是 `setCanGoBack$compose_webview`）
 * ⇒ **测试里没法把它设成 true** ⇒ 只能通过 [NavigationStateSource] 这一层喂进去。
 *
 * ## 鉴别力说明（这条很重要）
 *
 * 断言的是「**来源变成 true 之后，state holder 跟着变**」——
 * ⇒ 如果把 [NavigationStateEffect] 里的转发**删掉**，这两个用例会红
 * ⇒ 如果把转发写成**写死 false**，这两个用例会红
 * ⇒ ⇒ **断言有鉴别力**，不是空转。
 */
@OptIn(ExperimentalTestApi::class)
class NavigationStateEffectTest {

    /** 可控的导航状态来源（模拟 controller 的 `canGoBack` / `canGoForward`）。 */
    private class FakeNavigationStateSource : NavigationStateSource {
        var back by mutableStateOf(false)
        var forward by mutableStateOf(false)
        override val canGoBack: Boolean get() = back
        override val canGoForward: Boolean get() = forward
    }

    @Test
    fun syncsCanGoBackIntoStateHolder() = runComposeUiTest {
        val source = FakeNavigationStateSource()
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            NavigationStateEffect(source, stateHolder)
        }

        // 初始值：来源是 false ⇒ state holder 也应是 false
        assertFalse(stateHolder.canGoBack, "来源为 false 时不应同步出 true")
        assertFalse(stateHolder.canGoForward, "来源为 false 时不应同步出 true")

        // 来源变成 true ⇒ state holder 必须跟着变
        source.back = true
        waitForIdle()

        assertTrue(stateHolder.canGoBack, "来源 canGoBack=true 必须同步进 state holder")
    }

    @Test
    fun syncsCanGoForwardIntoStateHolder() = runComposeUiTest {
        val source = FakeNavigationStateSource()
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            NavigationStateEffect(source, stateHolder)
        }

        source.forward = true
        waitForIdle()

        assertTrue(stateHolder.canGoForward, "来源 canGoForward=true 必须同步进 state holder")
    }

    @Test
    fun reflectsTurnedOffNavigationState() = runComposeUiTest {
        val source = FakeNavigationStateSource()
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            NavigationStateEffect(source, stateHolder)
        }

        source.back = true
        source.forward = true
        waitForIdle()
        assertTrue(stateHolder.canGoBack)
        assertTrue(stateHolder.canGoForward)

        // 退回没有历史的状态 ⇒ state holder 必须跟着回 false
        // （否则"已经回到第一页还显示着可后退"这种假状态会一直挂着）
        source.back = false
        source.forward = false
        waitForIdle()

        assertFalse(stateHolder.canGoBack, "来源回 false 后 state holder 必须跟着回 false")
        assertFalse(stateHolder.canGoForward, "来源回 false 后 state holder 必须跟着回 false")
    }

    @Test
    fun updatesNavigationWithBothValuesAtOnce() = runComposeUiTest {
        val source = FakeNavigationStateSource()
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            NavigationStateEffect(source, stateHolder)
        }

        source.back = true
        source.forward = true
        waitForIdle()

        assertEquals(true, stateHolder.canGoBack)
        assertEquals(true, stateHolder.canGoForward)
    }
}
