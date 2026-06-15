package com.yuanjingtech.boot.app.kmp.ad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.dsl.module

/**
 * iOS 平台 AdMob 实现占位
 *
 * 当前使用 [NoOpAdManager],不集成任何原生 SDK。后续接入步骤:
 * 1. 在 `iosApp` 中通过 CocoaPods / Swift Package Manager 引入 `Google-Mobile-Ads-SDK`
 * 2. 在 `feature/ad/build.gradle.kts` 中添加 `kotlin("native.cocoapods")` 或
 *    使用 cinterop / swiftPMDependencies 暴露 SDK 给 iosMain
 * 3. 用 [UIKitView] 渲染 `GADBannerView`,通过 [GADInterstitialAd] / [ GADRewardedAd]
 *    加载并展示全屏广告
 */
actual val adPlatformModule = module {
    single<AdManager> { NoOpAdManager() }
    single<AdBannerRenderer> { IosAdBannerRenderer() }
}

actual val currentPlatform: PlatformType = PlatformType.IOS

private class IosAdBannerRenderer : AdBannerRenderer {
    @Composable
    override fun Render(modifier: Modifier, adUnitId: String) {
        // TODO: 使用 UIKitView { GADBannerView(adSize: kGADAdSizeBanner) } 接入真实广告
        PlaceholderBanner(
            modifier = modifier,
            label = "iOS Banner (未启用)",
        )
    }
}
