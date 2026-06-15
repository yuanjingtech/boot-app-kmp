package com.yuanjingtech.boot.app.kmp.ad

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

@OptIn(ExperimentalNativeApi::class, ExperimentalCoroutinesApi::class)
class NoOpAdManagerTest {

    @Test
    fun `initialize does not throw`() = runTest(UnconfinedTestDispatcher()) {
        val manager = NoOpAdManager()
        manager.initialize(AdUnitConfig.TEST)
    }

    @Test
    fun `preload does not throw`() = runTest(UnconfinedTestDispatcher()) {
        val manager = NoOpAdManager()
        manager.preload(AdType.BANNER)
        manager.preload(AdType.INTERSTITIAL)
        manager.preload(AdType.REWARDED)
    }

    @Test
    fun `show returns false on all types`() = runTest(UnconfinedTestDispatcher()) {
        val manager = NoOpAdManager()
        for (type in AdType.entries) {
            assertFalse(manager.show(type), "show($type) should return false on no-op platform")
        }
    }

    @Test
    fun `show emits LoadFailed event`() = runTest(UnconfinedTestDispatcher()) {
        val manager = NoOpAdManager()
        val deferred = backgroundScope.async { manager.events.first() }
        // 主动让订阅协程有机会启动
        kotlinx.coroutines.yield()
        manager.show(AdType.INTERSTITIAL)
        val event = deferred.await()
        assertIs<AdEvent.LoadFailed>(event)
        assertEquals(AdType.INTERSTITIAL, event.type)
    }
}
