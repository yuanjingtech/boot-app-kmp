package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.parkwoocheol.composewebview.WebContent
import com.parkwoocheol.composewebview.WebViewError
import com.parkwoocheol.composewebview.WebViewErrorType
import com.parkwoocheol.composewebview.WebViewState as LibraryWebViewState
import com.yuanjingtech.boot.app.kmp.webview.WebViewState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * 加载失败的**真实链路**测试 —— 真的碰库的 `WebViewState` 与库的 `WebViewError`。
 *
 * ## 要防的是什么回归
 *
 * 库的 `ComposeWebView` 在 Android / iOS 上是这样调 `errorContent` 的：
 * ```kotlin
 * if (state.errorsForCurrentRequest.isNotEmpty()) {
 *     errorContent(state.errorsForCurrentRequest)
 * }
 * ```
 * ⇒ ⇒ **列表为空 ⇒ slot 根本不会被调用** ⇒ ⇒ **永远递不到空列表**
 * ⇒ ⇒ boot 的 `WebViewState.error` **一旦设成非 null 就再也清不回 null**
 * ⇒ ⇒ `isReady`（= `!isLoading && error == null`）**永远为 false**
 * ⇒ ⇒ 消费方拿到的"加载失败"信号**粘住不放**、重试按钮按了也没用。
 *
 * ⇒ 唯一的清空路径 = **覆写库的 `ComposeWebViewClient.onPageStarted`**
 * （"新一轮加载开始"这个语义本来就由库定义、由 client 回调表达）。
 *
 * ## 为什么必须走真链路
 *
 * 旧的四个用例直接喂 `MutableLoadErrorSource` ⇒ **完全没碰库**
 * ⇒ ⇒ 覆盖不到"库那边的错误怎么进来"和"库那边的错误怎么清掉"
 * ⇒ ⇒ 上面那个缺陷正是从这条缝里漏过去的。
 */
@OptIn(ExperimentalTestApi::class)
class LoadErrorClearingClientTest {

    /**
     * 造一个带错误列表的库 state。
     *
     * ⚠️ 只用 `WebViewError(type=…, errorCode=…, description=…)` 这个 public 构造器
     * ⇒ **不要**用 `request` / `platformError` 那个 deprecated 构造器
     * （Android 上它收 Android 类型 ⇒ 在 jvmTest 里根本构造不出来）。
     */
    private fun libraryStateWithError(): LibraryWebViewState {
        val state = LibraryWebViewState(WebContent.NavigatorOnly)
        state.errorsForCurrentRequest.add(
            WebViewError(
                type = WebViewErrorType.HOST_LOOKUP,
                errorCode = -6,
                description = "主机未解析",
            )
        )
        return state
    }

    @Test
    fun libraryErrorReachesStateHolder() = runComposeUiTest {
        val libraryState = libraryStateWithError()
        val stateHolder = WebViewState(initialUrl = "https://example.com")
        val source = MutableLoadErrorSource()

        setContent {
            LoadErrorEffect(source, stateHolder)
        }

        // 库的 `errorContent` slot 的内容喂进来（这正是库在列表非空时做的事）
        applyLoadErrors(source, libraryState.errorsForCurrentRequest)
        waitForIdle()

        val error = assertNotNull(stateHolder.error, "库的错误必须进 state holder")
        assertEquals(-6, error.code)
        assertEquals("主机未解析", error.description)
        assertFalse(stateHolder.isReady, "有加载失败时不应是可交互状态")
    }

    @Test
    fun newPageStartClearsTheStuckError() = runComposeUiTest {
        val libraryState = libraryStateWithError()
        val stateHolder = WebViewState(initialUrl = "https://example.com")
        val source = MutableLoadErrorSource()

        setContent {
            LoadErrorEffect(source, stateHolder)
        }

        applyLoadErrors(source, libraryState.errorsForCurrentRequest)
        waitForIdle()
        assertNotNull(stateHolder.error, "前置条件：错误已经粘住了")

        // ⭐ 新一轮加载开始 ⇒ client 的 onPageStarted 被调 ⇒ 错误必须清掉
        val client = createLoadErrorClearingClient(source)
        client.webViewState = libraryState
        client.onPageStarted(null, "https://example.com/next", null)
        waitForIdle()

        assertNull(stateHolder.error, "新一轮加载开始后，粘住的错误必须被清掉")
        assertTrue(stateHolder.isReady, "错误清掉后必须回到可交互状态")
    }

    @Test
    fun newPageStartClearsOurOwnErrorSource() = runComposeUiTest {
        // ⚠️ **不要断言"库的列表被清空"** —— 那是**平台差异**：
        //   Android / iOS 的库实现会在 onPageStarted 里 `errorsForCurrentRequest.clear()`；
        //   **desktop 不清**（它的 onPageStarted 只写 loadingState / lastLoadedUrl）。
        // ⇒ ⇒ 这个用例只钉"**我们自己的** lastError 必须被清掉"，
        //     那是跨平台都成立的那一半。
        //
        // ⚠️ **必须先把错误设上** —— 否则 `lastError` 本来就是 null，
        //    断言 `assertNull` 会**恒绿**（变异测试实测踩到过：
        //    删掉修复后这个用例照样绿 ⇒ 没有鉴别力）。
        val libraryState = libraryStateWithError()
        val source = MutableLoadErrorSource()
        source.lastError = LoadError(code = -6, description = "主机未解析")

        val client = createLoadErrorClearingClient(source)
        client.webViewState = libraryState
        client.onPageStarted(null, "https://example.com/next", null)

        assertNull(source.lastError, "新一轮加载开始后，我们自己的 lastError 必须清空")
    }

    @Test
    fun superCallKeepsLibraryLastLoadedUrl() = runComposeUiTest {
        // `super.onPageStarted` 里库会写 loadingState / lastLoadedUrl
        // ⇒ ⇒ 漏掉 super ⇒ **T2.6 第 ① 条（导航状态）会一起坏掉**
        //
        // ⚠️ `lastLoadedUrl` 在 **Android / iOS / desktop 三个平台都会写**
        // ⇒ ⇒ 这是跨平台都成立的那一半 ⇒ 可以钉。
        val libraryState = LibraryWebViewState(WebContent.NavigatorOnly)
        val source = MutableLoadErrorSource()

        val client = createLoadErrorClearingClient(source)
        client.webViewState = libraryState
        client.onPageStarted(null, "https://example.com/next", null)

        assertEquals(
            "https://example.com/next",
            libraryState.lastLoadedUrl,
            "super.onPageStarted 必须被调用（否则库的 lastLoadedUrl 不会被写回）",
        )
    }
}
