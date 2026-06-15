package com.yuanjingtech.boot.app.kmp.ad

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 各平台共享的 [AdManager] 基础实现,提供事件流和默认 no-op 行为。
 */
abstract class BaseAdManager : AdManager {
    private val _events = MutableSharedFlow<AdEvent>(
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val events: SharedFlow<AdEvent> = _events.asSharedFlow()

    protected suspend fun emit(event: AdEvent) {
        _events.emit(event)
    }

    protected fun tryEmit(event: AdEvent): Boolean {
        return _events.tryEmit(event)
    }
}

/**
 * 占位 [AdManager] — 用于不支持的平台(JVM / JS / WasmJS)。
 *
 * 不初始化任何 SDK;调用 [show] 时直接返回 false 并发出 [AdEvent.LoadFailed]。
 */
class NoOpAdManager : BaseAdManager() {
    override suspend fun initialize(config: AdUnitConfig) {
        // no-op
    }

    override suspend fun preload(type: AdType) {
        // no-op
    }

    override suspend fun show(type: AdType): Boolean {
        emit(AdEvent.LoadFailed(type, "Ads are not supported on this platform"))
        return false
    }
}
