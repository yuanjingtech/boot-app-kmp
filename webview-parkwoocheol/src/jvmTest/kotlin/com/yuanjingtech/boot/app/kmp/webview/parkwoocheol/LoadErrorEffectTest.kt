package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.yuanjingtech.boot.app.kmp.webview.WebViewState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * [LoadErrorEffect] 的行为测试。
 *
 * ## 要防的是什么回归
 *
 * 库的 `ComposeWebView` 有 `onWebError: (List<WebViewError>) -> Unit` 参数
 * ⇒ 加载失败时库会把错误递出来
 * ⇒ 但 boot 的 `WebViewState.error` **永远是 null**
 * ⇒ 消费方（KMP 端"缓存条目加载失败后从列表移除"）拿不到失败信号。
 *
 * ⇒ 而这个缺陷**编译期完全看不出来**（`updateError` 没人调，编译照样过）
 * ⇒ ⇒ **只能靠测试兜住**。
 *
 * ## 为什么用假的来源而不是真的库错误
 *
 * 库的 `WebViewError` 类型在 boot 的 `webview` 模块（`WebViewState` 所在处）
 * **不可见** ⇒ 只能通过 [LoadErrorSource] 这一层喂进去。
 */
@OptIn(ExperimentalTestApi::class)
class LoadErrorEffectTest {

    @Test
    fun syncsLoadErrorIntoStateHolder() = runComposeUiTest {
        val source = MutableLoadErrorSource()
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            LoadErrorEffect(source, stateHolder)
        }

        // 初始：没有失败 ⇒ state holder 的 error 应为 null
        assertNull(stateHolder.error, "还没有失败时 error 应为 null")

        // 失败到来 ⇒ state holder 必须拿到
        source.lastError = LoadError(code = -6, description = "主机未解析")
        waitForIdle()

        val error = assertNotNull(stateHolder.error, "来源报失败后 state holder 必须有 error")
        assertEquals(-6, error.code)
        assertEquals("主机未解析", error.description)
    }

    @Test
    fun clearsErrorWhenSourceClearsIt() = runComposeUiTest {
        val source = MutableLoadErrorSource()
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            LoadErrorEffect(source, stateHolder)
        }

        source.lastError = LoadError(code = -6, description = "主机未解析")
        waitForIdle()
        assertNotNull(stateHolder.error)

        // ⚠️ 换一次地址重新加载 ⇒ 新的错误列表是空的
        // ⇒ state holder 的 error 必须跟着清掉（否则"上一次的失败"会一直挂着）
        source.lastError = null
        waitForIdle()

        assertNull(stateHolder.error, "来源清空后 state holder 的 error 必须跟着清空")
    }

    @Test
    fun reflectsLatestErrorOnly() = runComposeUiTest {
        val source = MutableLoadErrorSource()
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            LoadErrorEffect(source, stateHolder)
        }

        source.lastError = LoadError(code = -1, description = "第一次")
        waitForIdle()
        assertEquals(-1, assertNotNull(stateHolder.error).code)

        source.lastError = LoadError(code = -2, description = "第二次")
        waitForIdle()

        val error = assertNotNull(stateHolder.error)
        assertEquals(-2, error.code, "应保留最新一条错误")
        assertEquals("第二次", error.description)
    }

    @Test
    fun errorMakesStateNotReady() = runComposeUiTest {
        val source = MutableLoadErrorSource()
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            LoadErrorEffect(source, stateHolder)
        }

        // 没有错误 ⇒ isReady 为 true
        assertTrue(stateHolder.isReady, "没有错误时应当是可交互状态")

        source.lastError = LoadError(code = -6, description = "主机未解析")
        waitForIdle()

        // 有错误 ⇒ isReady 为 false（boot 的 `WebViewState.isReady` = !isLoading && error == null）
        assertFalse(stateHolder.isReady, "有加载失败时不应是可交互状态")
    }
}
