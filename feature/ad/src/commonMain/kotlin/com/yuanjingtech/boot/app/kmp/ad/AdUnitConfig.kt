package com.yuanjingtech.boot.app.kmp.ad

/**
 * 广告位类型
 */
enum class AdType {
    /** 横幅广告 */
    BANNER,

    /** 插屏广告 */
    INTERSTITIAL,

    /** 激励视频广告 */
    REWARDED,
}

/**
 * 广告位 ID 配置
 *
 * 每个广告位在 Android / iOS 上有独立的 ID,运行时由 [current] 字段选择当前平台的 ID。
 *
 * @param appIdAndroid Android 平台 AdMob App ID(对应 `AndroidManifest.xml` 中 `com.google.android.gms.ads.APPLICATION_ID`)
 * @param appIdIos iOS 平台 AdMob App ID(对应 `GADApplicationIdentifier` Info.plist 字段)
 * @param bannerAndroid / bannerIos
 * @param interstitialAndroid / interstitialIos
 * @param rewardedAndroid / rewardedIos
 */
data class AdUnitConfig(
    val appIdAndroid: String,
    val appIdIos: String,
    val bannerAndroid: String,
    val bannerIos: String,
    val interstitialAndroid: String,
    val interstitialIos: String,
    val rewardedAndroid: String,
    val rewardedIos: String,
) {
    fun idFor(type: AdType): String = when (currentPlatform) {
        PlatformType.ANDROID -> when (type) {
            AdType.BANNER -> bannerAndroid
            AdType.INTERSTITIAL -> interstitialAndroid
            AdType.REWARDED -> rewardedAndroid
        }
        PlatformType.IOS -> when (type) {
            AdType.BANNER -> bannerIos
            AdType.INTERSTITIAL -> interstitialIos
            AdType.REWARDED -> rewardedIos
        }
        PlatformType.OTHER -> ""
    }

    /**
     * 当前平台对应的 App ID
     */
    val appId: String
        get() = when (currentPlatform) {
            PlatformType.ANDROID -> appIdAndroid
            PlatformType.IOS -> appIdIos
            PlatformType.OTHER -> ""
        }

    companion object {
        /**
         * Google 官方测试 ID,仅用于开发阶段,避免误点真实广告。
         */
        val TEST = AdUnitConfig(
            appIdAndroid = "ca-app-pub-3940256099942544~3347511713",
            appIdIos = "ca-app-pub-3940256099942544~1458002511",
            bannerAndroid = "ca-app-pub-3940256099942544/6300978111",
            bannerIos = "ca-app-pub-3940256099942544/2934735716",
            interstitialAndroid = "ca-app-pub-3940256099942544/1033173712",
            interstitialIos = "ca-app-pub-3940256099942544/4411468910",
            rewardedAndroid = "ca-app-pub-3940256099942544/5224354917",
            rewardedIos = "ca-app-pub-3940256099942544/1712485313",
        )
    }
}

/**
 * 平台类型
 */
enum class PlatformType {
    ANDROID,
    IOS,
    OTHER,
}

/**
 * 当前运行平台 — 由各平台 actual 实现注入。
 */
expect val currentPlatform: PlatformType

/**
 * 激励视频奖励
 */
data class AdReward(
    val type: String,
    val amount: Int,
)
