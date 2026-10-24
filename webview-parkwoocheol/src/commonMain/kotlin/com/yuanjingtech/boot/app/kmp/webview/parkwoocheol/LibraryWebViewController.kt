package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import com.parkwoocheol.composewebview.WebViewController as LibraryController
import com.yuanjingtech.boot.app.kmp.webview.WebViewController

/**
 * boot 的 [WebViewController] 接口 ⇄ 库 `compose-webview` 的适配层 —— T2.7。
 *
 * ## 为什么必须有这一层
 *
 * boot 的 `WebViewController` 接口有 `goBack()` / `goForward()`，
 * 而库 `compose-webview` 1.8.2 **没有**这两个方法，它叫
 * **`navigateBack()` / `navigateForward()`**（见库的 `WebViewController.kt:206/221`）。
 *
 * ⇒ ⇒ 所以必须有这层翻译 ⇒ ⇒ 否则"前进/后退工具栏"根本无从谈起
 * ⇒ ⇒ T2.7 之前 boot 的 `WebViewController` 是个**悬空契约**（零实现方、零引用方）。
 *
 * ## ⚠️ 为什么这层放在 `webview-parkwoocheol`（而不是 `webview`）
 *
 * 库的 `WebViewController` 类型**不能**透到 boot 的 `webview` 模块
 * ⇒ ⇒ 那一层就得依赖 `compose-webview` ⇒ ⇒ 会把库提到 compile scope ⇒ ⇒ **依赖面无谓扩大**。
 *
 * ⇒ ⇒ 这一层留在**唯一依赖库的那个模块**里（`webview-parkwoocheol`），
 * 对外只暴露 boot 自己的 [WebViewController] 接口 ⇒ ⇒ **依赖面零扩大**。
 *
 * ## ⚠️ 方法映射为什么不能靠"名字像"来定
 *
 * 库的 `navigateBack()` / `navigateForward()` / `reload()` / `stopLoading()` / `clearCache()`
 * **全是异步的**（源码里都是 `coroutineScope.launch { navigationEvents.emit(...) }`）⇒ ⇒ **无返回值**。
 *
 * ⇒ ⇒ 「调了库的方法」这件事**没法靠返回值判断** ⇒ ⇒ 只能观察库发出的**导航事件**。
 * ⇒ ⇒ `LibraryWebViewControllerTest` 用反射读库私有事件流来验真。
 */
internal class LibraryWebViewController(
    private val controller: LibraryController,
) : WebViewController {

    override fun loadUrl(url: String) {
        controller.loadUrl(url)
    }

    override fun reload() {
        controller.reload()
    }

    override fun goBack() {
        controller.navigateBack()
    }

    override fun goForward() {
        controller.navigateForward()
    }

    override fun stopLoading() {
        controller.stopLoading()
    }

    override fun clearCache() {
        controller.clearCache()
    }
}
