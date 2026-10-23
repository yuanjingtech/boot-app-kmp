package com.yuanjingtech.boot.app.kmp.webview

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * [shouldShowProgressBar] 的行为测试 —— T2.8。
 *
 * ## 要防的是什么回归
 *
 * 原来的条件是 `state.isLoading || state.progress < 1f`
 * ⇒ 而 `updateProgress(...)` **全仓调用点数为 0** ⇒ `progress` **恒为 0**
 * ⇒ ⇒ `progress < 1f` **恒真** ⇒ 进度条**永远停在 0%、加载完也不消失**。
 *
 * ⇒ 验收标准（`tasks.md` 的 T2.8）：**要么消失、要么显示真实进度；不允许停 0% 且不消失**。
 *
 * ## 为什么判据要单独抽成一个函数
 *
 * ⚠️ 之前它是一个写在 `if` 里的表达式 ⇒ ⇒ **测不到**
 * ⇒ ⇒ 于是"完成那一刻掉回 0f"那个 bug 可以**悄悄溜过去**
 * ⇒ ⇒ **我自己的变异测试就是这么发现的**（把 `Finished -> 1f` 改成 `0f`，
 * 期望 3 个红、实际只有 1 个红 ⇒ ⇒ 说明另外两条用例没在看这个判据）。
 * ⇒ ⇒ 抽成函数 ⇒ ⇒ 两边都能测 ⇒ ⇒ **判据与用法不会走偏**。
 *
 * ## 鉴别力说明
 *
 * - 把上界从 `< 1f` 改成 `<= 1f` ⇒ 这些用例会红
 * - 把下界从 `> 0f` 改成 `>= 0f` ⇒ 这些用例会红
 * - 把整个判据写死成 `true` ⇒ 这些用例会红
 */
class ShouldShowProgressBarTest {

    @Test
    fun doesNotShowWhenProgressIsZero() {
        // ⚠️ desktop / js / wasmJs 上库的 onProgressChanged 只回调、不写 state
        // ⇒ ⇒ 那些平台的进度**恒为 0** ⇒ ⇒ **不显示**（满足验收标准「要么消失」）
        assertFalse(
            shouldShowProgressBar(0f),
            "进度为 0 时不显示（desktop 等平台取不到真进度 ⇒ 不该显示一个永远 0% 的假条）",
        )
    }

    @Test
    fun showsRealProgress() {
        assertTrue(shouldShowProgressBar(0.01f), "刚有一点真进度就该显示")
        assertTrue(shouldShowProgressBar(0.42f), "显示真进度")
        assertTrue(shouldShowProgressBar(0.99f), "显示真进度")
    }

    @Test
    fun hidesWhenProgressReachesOne() {
        // ⚠️ 实现层在加载完成时把 progress 写成 1f
        // ⇒ ⇒ 上界必须是 `< 1f` ⇒ ⇒ **否则进度条卡在满格、永不消失**
        assertFalse(
            shouldShowProgressBar(1f),
            "进度到 1f（加载完成）时必须隐藏 —— 否则进度条永远不消失",
        )
    }

    @Test
    fun hidesWhenProgressExceedsOne() {
        // 防御性：万一哪天实现层写了 > 1f 的值，也不该显示
        assertFalse(shouldShowProgressBar(1.5f), "超过 1f 不显示")
    }

    @Test
    fun hidesWhenProgressIsNegative() {
        // 防御性：负数进度不该显示
        assertFalse(shouldShowProgressBar(-0.1f), "负数进度不显示")
    }
}
