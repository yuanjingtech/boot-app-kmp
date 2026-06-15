package com.yuanjingtech.boot.app.kmp.ad

import kotlinx.coroutines.flow.SharedFlow

/**
 * 广告管理器
 *
 * 封装平台 AdMob SDK,提供统一的初始化、加载、展示、事件订阅接口。
 * 各平台 actual 实现见 [androidMain] / [iosMain] / [jvmMain] / [jsMain] / [wasmJsMain]。
 */
interface AdManager {
    /** 事件流 */
    val events: SharedFlow<AdEvent>

    /**
     * 初始化 SDK,建议在 App 启动时调用一次。
     *
     * @param config 广告位配置
     */
    suspend fun initialize(config: AdUnitConfig)

    /**
     * 预加载广告(可选,提前加载可以减少展示延迟)。
     */
    suspend fun preload(type: AdType)

    /**
     * 展示广告。
     *
     * @param type 广告位类型
     * @return true 表示已发起展示;false 表示当前没有可展示的广告
     */
    suspend fun show(type: AdType): Boolean
}
