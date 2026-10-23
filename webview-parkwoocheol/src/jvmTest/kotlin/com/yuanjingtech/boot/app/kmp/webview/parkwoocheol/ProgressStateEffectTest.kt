package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.parkwoocheol.composewebview.LoadingState
import com.parkwoocheol.composewebview.WebContent
import com.parkwoocheol.composewebview.WebViewState as LibraryWebViewState
import com.yuanjingtech.boot.app.kmp.webview.WebViewState
import com.yuanjingtech.boot.app.kmp.webview.shouldShowProgressBar
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * [ProgressStateEffect] 的行为测试 —— T2.8。
 *
 * ## 要防的是什么回归
 *
 * `WebViewState.updateProgress(...)` **早就有**，但全仓**调用点数为 0**
 * ⇒ `WebViewState.progress` **恒为 0**。
 *
 * ⇒ 后果：`webview` 模块的进度条条件 `state.isLoading || state.progress < 1f`
 * ⇒ ⇒ **`progress < 1f` 恒真** ⇒ 进度条**永远停在 0%、加载完也不消失**。
 *
 * ⇒ 而这个缺陷**编译期完全看不出来** ⇒ ⇒ 只能靠测试兜住。
 *
 * ## 为什么用假的来源而不是真的库 state
 *
 * 库的 `WebViewState.loadingState` 是 **public getter + internal setter**
 * ⇒ ⇒ **测试里没法把它设成 `LoadingState.Loading(0.42f)`**
 * ⇒ ⇒ 只能通过 [ProgressSource] 这一层喂进去（与 [NavigationStateEffectTest] 同一个套路）。
 *
 * ## 鉴别力说明（这条很重要）
 *
 * 断言的是「**来源的进度变了之后，state holder 跟着变**」
 * ⇒ 删掉 [ProgressStateEffect] 里的转发 ⇒ 这些用例会红
 * ⇒ 把转发写成**写死 0f** ⇒ 这些用例会红
 * ⇒ ⇒ **断言有鉴别力**，不是空转。
 *
 * ⚠️ **断言"为 0"之前必须先断言"脏"** ——
 * 否则一个恒为 0 的实现也能让 `assertEquals(0f, ...)` 绿 ⇒ **没有鉴别力**。
 */
@OptIn(ExperimentalTestApi::class)
class ProgressStateEffectTest {

    /** 可控的进度来源（模拟库的 `loadingState`）。 */
    private class FakeProgressSource : ProgressSource {
        var value by mutableStateOf(0f)
        override val progress: Float get() = value
    }

    @Test
    fun syncsProgressIntoStateHolder() = runComposeUiTest {
        val source = FakeProgressSource()
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            ProgressStateEffect(source, stateHolder)
        }
        waitForIdle()

        source.value = 0.42f
        waitForIdle()

        assertEquals(
            0.42f,
            stateHolder.progress,
            "来源的进度必须同步进 state holder",
        )
    }

    @Test
    fun reflectsProgressGoingBackToZero() = runComposeUiTest {
        val source = FakeProgressSource()
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            ProgressStateEffect(source, stateHolder)
        }

        source.value = 0.7f
        waitForIdle()
        assertEquals(0.7f, stateHolder.progress, "先断言'脏'：此刻确实有进度")

        // 退回 0（新一轮加载开始）⇒ state holder 必须跟着回 0
        source.value = 0f
        waitForIdle()

        assertEquals(0f, stateHolder.progress, "来源回 0 后 state holder 必须跟着回 0")
    }

    @Test
    fun progressDefaultsToZeroBeforeAnyEffectRuns() = runComposeUiTest {
        // 默认值必须是 0 —— 进度条不该在"还没开始"时就显示
        val stateHolder = WebViewState(initialUrl = "https://example.com")
        assertEquals(0f, stateHolder.progress, "默认进度是 0")
    }

    @Test
    fun readsZeroFromLibraryStateThatIsNotLoading() = runComposeUiTest {
        // 真实链路的读法：库的 state 读 `loadingState` 里 `Loading.progress`
        // ⇒ 新建 state 的 `loadingState` 是 `Initializing` ⇒ 取不到百分比 ⇒ 0f
        val libraryState = LibraryWebViewState(WebContent.NavigatorOnly)
        val source = LibraryProgressSource(libraryState = libraryState)
        val stateHolder = WebViewState(initialUrl = "https://example.com")

        setContent {
            ProgressStateEffect(source, stateHolder)
        }
        waitForIdle()

        assertEquals(
            0f,
            stateHolder.progress,
            "`Initializing` 不是 `Loading` ⇒ 取不到百分比 ⇒ 必须是 0f",
        )
    }

    /**
     * ⭐⭐ **这条是 T2.8 最容易漏的坑**（@远景react native开发 在 2026-10-24 核出来的）。
     *
     * ## 问题
     *
     * 库的 Android / iOS 实现在进度到 100 时**写 `Finished` 而不是 `Loading(1f)`**：
     * ```kotlin
     * // Android
     * state?.loadingState = if (newProgress == 100) LoadingState.Finished
     *                         else LoadingState.Loading(newProgress / 100f)
     * ```
     * ⇒ ⇒ **完成那一刻 `loadingState` 已经不是 `Loading` 了** ⇒ ⇒ `Loading.progress` **取不到**
     * ⇒ ⇒ 若按"不是 `Loading` 就 0f"处理 ⇒ **进度会从 0.99 掉回 0**
     * ⇒ ⇒ 后果：`0f < progress < 1f` 的判据又变 false ⇒ **进度条在 99% 卡住不消失**。
     *
     * ## 本实现的约定
     *
     * **读到 `Finished` 时返回 `1f`**（表示"到头了"）
     * ⇒ ⇒ 这样 `webview` 模块的判据 `0f < progress < 1f` 在完成时变 false ⇒ **条会消失** ✅
     *
     * ⚠️ 这条直接测 [progressOf]（纯函数）⇒ **不需要造库的 state**
     * ⇒ ⇒ 因为库的 `loadingState` setter 是 `internal` ⇒ 根本造不出 `Finished` 的 state。
     */
    @Test
    fun finishedStateIsReadAsFullProgressNotZero() {
        assertEquals(
            1f,
            progressOf(LoadingState.Finished),
            "⚠️ `Finished` 必须读成 1f（不是 0f）—— 否则进度会从 0.99 掉回 0、进度条卡住不消失",
        )
    }

    @Test
    fun loadingStateIsReadAsItsOwnProgress() {
        assertEquals(0.42f, progressOf(LoadingState.Loading(0.42f)), "百分比原样透传")
        assertEquals(0f, progressOf(LoadingState.Loading(0f)), "0% 就是 0f")
        // ⚠️ 库在 100 时就切到 Finished 了 ⇒ `Loading.progress` **最大只到 0.99**
        assertEquals(0.99f, progressOf(LoadingState.Loading(0.99f)), "库能给出的最大值是 0.99")
    }

    @Test
    fun statesWithoutProgressAreReadAsZero() {
        assertEquals(
            0f,
            progressOf(LoadingState.Initializing),
            "`Initializing` 没有百分比 ⇒ 0f（不能当成'0% 但在加载'）",
        )
    }

    /**
     * ⭐ **端到端契约**：走一遍真实的进度序列 ⇒ **每一帧的值与可见性都有定论**。
     *
     * ⇒ ⇒ 这条测试钉住的是"**进度条能出现、也能消失**"这个验收标准。
     *
     * ⚠️⚠️ **判据必须用生产代码里那个**（`shouldShowProgressBar`），
     * **不能在这里手写一份** ——
     * ⚠️ 我第一次就是手写的 ⇒ ⇒ 结果"把 `Finished` 读成 0f"那个变异**只让 1 条用例红**
     * ⇒ ⇒ **手写的判据和生产判据会走偏** ⇒ ⇒ 手写的那份是假的保护。
     *
     * ⚠️⚠️ **只断言"可见性"也不够** ——
     * ⚠️ 我第二次只断言可见性 ⇒ ⇒ **还是只让 1 条用例红**！
     * ⇒ ⇒ 原因：`Finished` 读成 `0f` 和读成 `1f`，**可见性都是"不显示"**
     * ⇒ ⇒ **光看"显不显示"区分不出来** ⇒ ⇒ **必须断言"值"**。
     * ⇒ ⇒ **这才是那个 bug 的真正差别**：
     * - 读成 `0f` ⇒ 进度值**从 0.99 倒退回 0** ⇒ **进度条会倒退**
     * - 读成 `1f` ⇒ 进度值**前进到 1.0** ⇒ **进度条填满再消失**（这才是对的）
     */
    @Test
    fun progressBarFollowsTheRealProgressSequence() {
        // 真实的 Android 序列：onPageStarted 写 Loading(0f) → 逐级 0.42 → 0.99 → 完成写 Finished
        val sequence = listOf(
            LoadingState.Loading(0f),
            LoadingState.Loading(0.42f),
            LoadingState.Loading(0.99f),
            LoadingState.Finished,
        )
        val values = sequence.map { progressOf(it) }
        val visible = values.map { shouldShowProgressBar(it) }

        assertEquals(
            listOf(0f, 0.42f, 0.99f, 1f),
            values,
            "⚠️ 进度值必须**单调前进**并以 1f 收尾 —— " +
                "若 `Finished` 读成 0f，进度会从 0.99 **倒退**回 0（进度条会倒退）",
        )
        assertEquals(
            listOf(false, true, true, false),
            visible,
            "进度条序列：开始不显示 → 显示真进度 → 显示真进度 → 填满后消失",
        )
    }
}
