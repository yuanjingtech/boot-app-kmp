package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import com.parkwoocheol.composewebview.WebContent
import com.parkwoocheol.composewebview.WebViewController as LibraryController
import com.parkwoocheol.composewebview.WebViewState as LibraryWebViewState
import java.lang.reflect.Method
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * [LibraryWebViewController] —— boot 的 [com.yuanjingtech.boot.app.kmp.webview.WebViewController]
 * 接口到库 `compose-webview` 的适配层 —— T2.7。
 *
 * ## 这个适配层解决什么
 *
 * boot 的 `WebViewController` 接口有 `goBack()` / `goForward()`，
 * 而库 `compose-webview` 1.8.2 **没有**这两个方法，它叫
 * **`navigateBack()` / `navigateForward()`**。
 *
 * ⇒ ⇒ 所以必须有这层翻译 ⇒ ⇒ 否则「前进/后退工具栏」根本无从谈起。
 *
 * ## ⚠️ 为什么这个测试要用反射
 *
 * 库的 `navigationEvents` 是 **private** 字段，
 * `NavigationEvent` 是 **private sealed interface** ⇒ **外部既读不到事件、也引用不了事件类型**。
 *
 * ⇒ ⇒ 想知道「`goBack()` 到底有没有真的让库去后退」，只有两条路：
 * ① 反射读库的私有事件流
 * ② 造一个真 WebView 看它有没有后退 —— **库里 `expect class WebView` 各平台 actual 不同，
 *    测试里造不出真 WebView** ⇒ ② 不可行。
 *
 * ⇒ ⇒ 走 ①。反射链路（**javap 实测确认**）：
 * ```
 * WebViewController.access$getNavigationEvents$p(controller)  → ReplayOnceEventFlow
 * ReplayOnceEventFlow.access$getFlow$p(flow)                   → MutableSharedFlow
 * MutableSharedFlow.getReplayCache()                          → List<NavigationEvent>
 * ```
 * 事件的 class 简单名 = `WebViewController$NavigationEvent$Back` / `$Forward` / `$Reload` / …
 *
 * ## ⚠️ 反射本身也要被验证（否则两条错误会互相掩盖）
 *
 * 「映射写错了」和「反射读错了」这两种错误，如果混在同一个断言里，
 * **会互相掩盖**：比如映射把 `goBack` 写成了 `goForward`，反射又把 Forward 读成了 Back
 * ⇒ ⇒ 两个错误抵消，测试照样绿。
 *
 * ⇒ ⇒ 所以下面拆成两类用例：
 * ① **映射用例**：调用 boot 接口 ⇒ 断言事件流的 class 简单名
 * ② **反射链路用例**：直接调库自己的 `navigateBack()` ⇒ 断言拿到的是 `Back`
 *    ⇒ 这条证明「反射链路没走偏」，它是①的基准。
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LibraryWebViewControllerTest {

    // ---------------------------------------------------------------------
    // 反射工具
    // ---------------------------------------------------------------------

    /**
     * 取库的私有事件流。
     *
     * ⚠️ `access$getNavigationEvents$p` 是 Kotlin 编译器生成的 **synthetic** 静态桥接方法
     * （javap 实测 `flags: ACC_PUBLIC, ACC_STATIC, ACC_FINAL, ACC_SYNTHETIC`）
     * ⇒ ⇒ **必须用 `setAccessible(true)` 才拿得到**。
     */
    private fun navigationEvents(controller: LibraryController): MutableSharedFlow<Any> {
        val accessMethod = LibraryController::class.java.getDeclaredMethod(
            "access\$getNavigationEvents\$p",
            LibraryController::class.java,
        )
        accessMethod.isAccessible = true
        val replayFlow = accessMethod.invoke(null, controller)

        val flowMethod = replayFlow.javaClass.getDeclaredMethod(
            "access\$getFlow\$p",
            replayFlow.javaClass,
        )
        flowMethod.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return flowMethod.invoke(null, replayFlow) as MutableSharedFlow<Any>
    }

    /**
     * 反射调用库的 `bindState`。
     *
     * ⚠️ 库的 `bindState` 在 Kotlin 源码里是 **`internal`**（`WebViewController.kt:508`）
     * ⇒ JVM 上被改名成 **`bindState$compose_webview`**（javap 实测）
     * ⇒ ⇒ **从别的模块不能直接调** ⇒ ⇒ 只能反射。
     *
     * ⚠️ **为什么要绑 state**：库的 `loadUrl` 走 `updateTopLevelContent()`
     * ⇒ 它 `webViewState?.updateContentRequest(content) ?: run { pendingTopLevelContent = content }`
     * ⇒ ⇒ **没绑 state 的话，URL 只进了 `pendingTopLevelContent`，外部根本看不到**
     * ⇒ ⇒ **不绑就断言 ⇒ 恒绿（没有鉴别力）**。
     */
    private fun bindState(controller: LibraryController, state: LibraryWebViewState) {
        val method: Method = LibraryController::class.java.getDeclaredMethod(
            "bindState\$compose_webview",
            LibraryWebViewState::class.java,
        )
        method.isAccessible = true
        method.invoke(controller, state)
    }

    /**
     * 读事件流里**最后一个**事件的**全限定类名**。
     *
     * ⚠️ 库的 `ReplayOnceEventFlow` 底层是 `MutableSharedFlow(replay = 1)`
     * ⇒ ⇒ **replay cache 里最多 1 个元素** ⇒ ⇒ 取最后一个就是刚发的那一个。
     *
     * ⚠️ **不要在这里同时 collect** —— 库的 `collect` 会在收到值后
     * `flow.resetReplayCache()` ⇒ ⇒ 会把 replay cache 清空 ⇒ 读不到东西。
     *
     * ⚠️ **为什么用 `javaClass.name` 而不是 `simpleName`**：
     * 库的 `NavigationEvent` 是 **private sealed interface** ⇒ 外部**不能**引用它的类型
     * ⇒ ⇒ 只能靠类名字符串判类型。
     * ⚠️ `simpleName` 对嵌套类**只返回最后一段**（实测：`Back`，不是 `...$NavigationEvent$Back`）
     * ⇒ ⇒ 用 `name`（含 `$` 的全名）才够精确、能区分 `Back` 和 `NavigationEvent$Back`。
     */
    private fun lastEventName(controller: LibraryController): String? {
        val cache = navigationEvents(controller).replayCache
        assertTrue(cache.isNotEmpty(), "事件流里应该有一个事件（replay=1 ⇒ 最多一个）")
        return cache.last().javaClass.name
    }

    // ---------------------------------------------------------------------
    // ① 反射链路本身的正确性（基准）
    // ---------------------------------------------------------------------

    @Test
    fun reflectionPathReadsTheRealNavigationEvent() = runTest {
        // 这条是①的基准：直接调**库自己的** navigateBack()
        // ⇒ ⇒ 反射读到的必须是 Back
        // ⇒ ⇒ 如果这条红了，说明是「反射读错了」，而不是「映射写错了」
        val controller = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

        controller.navigateBack()

        assertEquals("com.parkwoocheol.composewebview.WebViewController\$NavigationEvent\$Back", lastEventName(controller))
    }

    @Test
    fun reflectionPathReadsForwardAndReloadSeparately() = runTest {
        // 再验两个 ⇒ 把「class 简单名读串了」这种错误也排掉
        val controller = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

        controller.navigateForward()
        assertEquals("com.parkwoocheol.composewebview.WebViewController\$NavigationEvent\$Forward", lastEventName(controller))

        controller.reload()
        assertEquals("com.parkwoocheol.composewebview.WebViewController\$NavigationEvent\$Reload", lastEventName(controller))
    }

    // ---------------------------------------------------------------------
    // ② 映射：boot 接口 → 库方法
    // ---------------------------------------------------------------------

    @Test
    fun goBackMapsToLibraryNavigateBack() = runTest {
        val libraryController = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        val controller = LibraryWebViewController(libraryController)

        controller.goBack()

        assertEquals("com.parkwoocheol.composewebview.WebViewController\$NavigationEvent\$Back", lastEventName(libraryController))
    }

    @Test
    fun goForwardMapsToLibraryNavigateForward() = runTest {
        val libraryController = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        val controller = LibraryWebViewController(libraryController)

        controller.goForward()

        assertEquals("com.parkwoocheol.composewebview.WebViewController\$NavigationEvent\$Forward", lastEventName(libraryController))
    }

    @Test
    fun goBackDoesNotEmitForward() = runTest {
        // ⚠️ 「前后不能搞反」是最容易犯、也最难发现的错
        // ⇒ ⇒ 单独钉一条：调 goBack ⇒ 事件**不能**是 Forward
        val libraryController = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        val controller = LibraryWebViewController(libraryController)

        controller.goBack()

        assertNotEqualsForward(lastEventName(libraryController))
    }

    @Test
    fun goForwardDoesNotEmitBack() = runTest {
        val libraryController = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        val controller = LibraryWebViewController(libraryController)

        controller.goForward()

        val name = assertNotNull(lastEventName(libraryController), "应该有事件")
        assertTrue(
            !name.endsWith("NavigationEvent\$Back"),
            "goForward 不该发出 Back 事件，实际是 $name",
        )
    }

    @Test
    fun reloadMapsToLibraryReload() = runTest {
        val libraryController = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        val controller = LibraryWebViewController(libraryController)

        controller.reload()

        assertEquals("com.parkwoocheol.composewebview.WebViewController\$NavigationEvent\$Reload", lastEventName(libraryController))
    }

    @Test
    fun stopLoadingMapsToLibraryStopLoading() = runTest {
        val libraryController = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        val controller = LibraryWebViewController(libraryController)

        controller.stopLoading()

        assertEquals("com.parkwoocheol.composewebview.WebViewController\$NavigationEvent\$StopLoading", lastEventName(libraryController))
    }

    @Test
    fun clearCacheMapsToLibraryClearCache() = runTest {
        val libraryController = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        val controller = LibraryWebViewController(libraryController)

        controller.clearCache()

        assertEquals("com.parkwoocheol.composewebview.WebViewController\$NavigationEvent\$ClearCache", lastEventName(libraryController))
    }

    @Test
    fun loadUrlWritesTheUrlIntoTheLibraryState() = runTest {
        // ⚠️ **`loadUrl` 是库里的例外**：
        //   库的 `loadUrl` **不发导航事件**（它走 `updateTopLevelContent(WebContent.Url(...))`）
        // ⇒ ⇒ 所以这条**不能**断言事件，只能断言「URL 被写进了库的 state」。
        //
        // ⚠️ 而且：**没绑 state 时**库的 `loadUrl` 只会把内容存进 `pendingTopLevelContent`
        // ⇒ ⇒ **必须先 `bindState`** 才能观察到（`bindState` 是 public 的）。
        val libraryController = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        val libraryState = LibraryWebViewState(WebContent.NavigatorOnly)
        bindState(libraryController, libraryState)

        val controller = LibraryWebViewController(libraryController)
        controller.loadUrl("https://example.com/page")

        val content = libraryState.content
        assertIs<WebContent.Url>(content, "loadUrl 之后库 state 里应该是 Url 类型的内容")
        assertEquals("https://example.com/page", content.url)
    }

    @Test
    fun loadUrlWithEmptyHeadersStillWritesTheUrl() = runTest {
        // boot 的 `loadUrl(url)` 只有一个参数 ⇒ 头必须传空 map
        // ⇒ ⇒ 钉住「没把头传歪」
        val libraryController = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        val libraryState = LibraryWebViewState(WebContent.NavigatorOnly)
        bindState(libraryController, libraryState)

        val controller = LibraryWebViewController(libraryController)
        controller.loadUrl("https://example.com/second")

        val content = assertIs<WebContent.Url>(libraryState.content)
        assertEquals("https://example.com/second", content.url)
        assertTrue(content.additionalHttpHeaders.isEmpty(), "不该凭空加请求头")
    }

    // ---------------------------------------------------------------------
    // ③ 适配层是 boot 接口的一个实现
    // ---------------------------------------------------------------------

    @Test
    fun adapterIsABootWebViewController() = runTest {
        // 编译期就能保证，但显式写一条 ⇒ 意图明确：
        // 这层就是 boot `WebViewController` 接口的实现（那个接口在 T2.7 之前是悬空的）
        val libraryController = LibraryController(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        val controller: com.yuanjingtech.boot.app.kmp.webview.WebViewController =
            LibraryWebViewController(libraryController)

        controller.goBack()

        assertEquals("com.parkwoocheol.composewebview.WebViewController\$NavigationEvent\$Back", lastEventName(libraryController))
    }

    private fun assertNotEqualsForward(eventName: String?) {
        assertTrue(
            eventName != null && !eventName.endsWith("NavigationEvent\$Forward"),
            "goBack 不该发出 Forward 事件，实际是 $eventName",
        )
    }
}
