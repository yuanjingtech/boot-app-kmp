package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.parkwoocheol.composewebview.WebContent
import com.parkwoocheol.composewebview.WebViewStateSaver
import com.parkwoocheol.composewebview.WebViewState as LibraryWebViewState
import com.yuanjingtech.boot.app.kmp.webview.WebViewState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * [WebViewStateSyncEffect] 的行为测试 —— **真实链路**（真的碰库的 `WebViewState`）。
 *
 * ## 要防的是什么回归
 *
 * 库里 `snapshotFlow{...}.collect{...}` **永不返回**
 * ⇒ 把两个 `collect` 写在**同一个** `LaunchedEffect` 里 ⇒ **第二个永远不执行**
 * ⇒ ⇒ `updatePageTitle` 永远不被调用 ⇒ `WebViewState.pageTitle` **恒为 null**
 * ⇒ ⇒ KMP 侧的页面标题**永远不显示**。
 *
 * ⚠️ **这个缺陷编译期完全看不出来**（`updatePageTitle` 有定义、调用点也在）
 * ⇒ ⇒ 只能靠测试兜住。
 *
 * ## 为什么要走 `WebViewStateSaver.restore`
 *
 * 库的 `WebViewState.pageTitle` 是 **public getter + internal setter**
 * ⇒ **测试不能直接写**
 * ⇒ ⇒ 唯一的公开写入口 = `WebViewStateSaver.restore(map)`
 * （`mapSaver` 的 restore 会写 `pageTitle` / `lastLoadedUrl` / `loadingState`）。
 *
 * ⚠️ map 的 key 必须**逐字**照抄库的源码里那几个常量
 * （`pageTitleKey="pagetitle"` / `lastLoadedUrlKey="lastloaded"` /
 *  `stateBundleKey="bundle"` / `contentKey="content"` /
 *  `suppressLastLoadedUrlFallbackKey="suppresslastloadedurlfallback"`）。
 */
@OptIn(ExperimentalTestApi::class)
class WebViewStateSyncEffectTest {

    /**
     * 造一个"标题已就绪"的库 state。
     *
     * ## ⚠️ `mapSaver` 的 restore 吃的是**扁平 List**，不是 Map
     *
     * 库的 `WebViewStateSaver` 是 `mapSaver(save=…, restore=…)`，
     * 而 `mapSaver` 内部是 `listSaver`：
     * ```kotlin
     * fun <T> mapSaver(save: …, restore: (Map<String, Any?>) -> T?) = listSaver(
     *     save = { mutableListOf<Any?>().apply { save(it).forEach { add(it.key); add(it.value) } } },
     *     restore = { list -> /* 把扁平 list 折回 map，再交给 restore */ },
     * )
     * ```
     * ⇒ ⇒ `Saver.restore(value)` 的 `value` 必须是 **key,value,key,value… 的扁平 List**
     * ⇒ ⇒ 传 Map 会抛 `ClassCastException: LinkedHashMap cannot be cast to java.util.List`。
     *
     * ⚠️ map 的 key 必须**逐字**照抄库的源码里那几个常量
     * （`pageTitleKey="pagetitle"` / `lastLoadedUrlKey="lastloaded"` /
     *  `stateBundleKey="bundle"` / `contentKey="content"` /
     *  `suppressLastLoadedUrlFallbackKey="suppresslastloadedurlfallback"`）。
     *
     * ⚠️ `content` 传 null ⇒ 库的 `toRestoredWebContent()` 返回 null
     * ⇒ ⇒ 回落到 `WebContent.NavigatorOnly`（不需要真的去加载）。
     */
    private fun libraryStateWithTitle(title: String?): LibraryWebViewState {
        val restored = WebViewStateSaver.restore(
            listOf(
                "pagetitle", title,
                "lastloaded", null,
                "bundle", null,
                "content", null,
                "suppresslastloadedurlfallback", false,
            )
        )
        checkNotNull(restored) { "WebViewStateSaver.restore 不该返回 null" }
        return restored
    }

    @Test
    fun syncsPageTitleFromLibraryState() = runComposeUiTest {
        val libraryState = libraryStateWithTitle("示例标题")
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            WebViewStateSyncEffect(libraryState = libraryState, stateHolder = stateHolder)
        }
        waitForIdle()

        assertEquals(
            "示例标题",
            stateHolder.pageTitle,
            "库 state 的 pageTitle 必须同步进 boot 的 state holder",
        )
    }

    @Test
    fun syncsLoadingStateFromLibraryState() = runComposeUiTest {
        // 库的新建 state 默认 `loadingState = Initializing`
        // ⇒ `isLoading = (loadingState !is Finished)` = true
        val libraryState = LibraryWebViewState(WebContent.NavigatorOnly)
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            WebViewStateSyncEffect(libraryState = libraryState, stateHolder = stateHolder)
        }
        waitForIdle()

        assertEquals(
            libraryState.isLoading,
            stateHolder.isLoading,
            "加载状态必须与库的口径一致（库：loadingState !is Finished）",
        )
    }

    @Test
    fun pageTitleStaysNullWhenLibraryHasNoTitle() = runComposeUiTest {
        val libraryState = libraryStateWithTitle(null)
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            WebViewStateSyncEffect(libraryState = libraryState, stateHolder = stateHolder)
        }
        waitForIdle()

        assertNull(stateHolder.pageTitle, "库里没有标题时，state holder 也不该有标题")
    }
}
