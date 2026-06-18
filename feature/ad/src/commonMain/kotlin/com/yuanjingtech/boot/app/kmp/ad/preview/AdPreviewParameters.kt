package com.yuanjingtech.boot.app.kmp.ad.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yuanjingtech.boot.app.kmp.ad.AdType
import com.yuanjingtech.boot.app.kmp.ad.AdUnitConfig

/**
 * AdType 变体 — 用于展示所有广告位的 banner 预览
 */
class AdTypeParameterProvider : PreviewParameterProvider<AdType> {
    override val values: Sequence<AdType> = AdType.entries.asSequence()
}

/**
 * AdUnitConfig 变体 — 同时支持多平台和 debug/release
 */
class AdUnitConfigParameterProvider : PreviewParameterProvider<AdUnitConfig> {
    override val values: Sequence<AdUnitConfig> = sequenceOf(
        // Google 官方测试 ID(仅 debug 模式)
        AdUnitConfig.TEST,
        // 示例生产配置(空 ID,模拟 release 模式)
        AdUnitConfig(
            appIdAndroid = "ca-app-pub-XXXXXXXX~YYYYYY",
            appIdIos = "ca-app-pub-XXXXXXXX~ZZZZZZ",
            bannerAndroid = "ca-app-pub-XXXXXXXX/AAAAAAAA",
            bannerIos = "ca-app-pub-XXXXXXXX/BBBBBBBB",
            interstitialAndroid = "ca-app-pub-XXXXXXXX/CCCCCCCC",
            interstitialIos = "ca-app-pub-XXXXXXXX/DDDDDDDD",
            rewardedAndroid = "ca-app-pub-XXXXXXXX/EEEEEEEE",
            rewardedIos = "ca-app-pub-XXXXXXXX/FFFFFFFF",
        ),
    )
}

/**
 * 多平台变体 — 用于演示同一 config 在不同平台下的 ID 选择
 */
class PlatformParameterProvider : PreviewParameterProvider<PlatformPreview> {
    override val values: Sequence<PlatformPreview> = sequenceOf(
        PlatformPreview(
            name = "android",
            config = AdUnitConfig(
                appIdAndroid = "ca-app-pub-AAA",
                appIdIos = "ca-app-pub-III",
                bannerAndroid = "android-banner",
                bannerIos = "ios-banner",
                interstitialAndroid = "android-interstitial",
                interstitialIos = "ios-interstitial",
                rewardedAndroid = "android-rewarded",
                rewardedIos = "ios-rewarded",
            ),
        ),
        PlatformPreview(
            name = "ios",
            config = AdUnitConfig(
                appIdAndroid = "ca-app-pub-AAA",
                appIdIos = "ca-app-pub-III",
                bannerAndroid = "android-banner",
                bannerIos = "ios-banner",
                interstitialAndroid = "android-interstitial",
                interstitialIos = "ios-interstitial",
                rewardedAndroid = "android-rewarded",
                rewardedIos = "ios-rewarded",
            ),
        ),
    )
}

/**
 * 平台预览数据
 */
data class PlatformPreview(
    val name: String,
    val config: AdUnitConfig,
)
