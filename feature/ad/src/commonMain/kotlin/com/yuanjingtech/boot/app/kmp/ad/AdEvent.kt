package com.yuanjingtech.boot.app.kmp.ad

/**
 * 广告事件
 */
sealed interface AdEvent {
    /** 广告位类型 */
    val type: AdType

    /** 加载成功 */
    data class Loaded(override val type: AdType) : AdEvent

    /** 加载失败 */
    data class LoadFailed(
        override val type: AdType,
        val message: String,
    ) : AdEvent

    /** 广告已展示 */
    data class Shown(override val type: AdType) : AdEvent

    /** 广告被点击 */
    data class Clicked(override val type: AdType) : AdEvent

    /** 广告关闭 */
    data class Dismissed(override val type: AdType) : AdEvent

    /** 激励视频发放奖励 */
    data class Rewarded(
        override val type: AdType,
        val reward: AdReward,
    ) : AdEvent

    /** 展示失败 */
    data class ShowFailed(
        override val type: AdType,
        val message: String,
    ) : AdEvent
}
