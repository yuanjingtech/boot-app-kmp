package com.yuanjingtech.boot.app.kmp.ad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.dsl.module

/**
 * iOS 平台 AdMob 接入(Swift Package Manager 方式)
 *
 * ## 集成步骤
 *
 * ### 1. 在 iOS app 工程中添加 SwiftPM 依赖
 *
 * 在 `iosApp/iosApp.xcodeproj` 中通过 Xcode 添加 Swift Package:
 *
 *   File → Add Package Dependencies…
 *   URL: https://github.com/googleads/swift-package-manager-google-mobile-ads
 *   依赖:`GoogleMobileAds`(对应 SPM package: `googleads/swift-package-manager-google-mobile-ads`)
 *
 * 或在 `iosApp/Package.swift`(若使用 SPM 形式 app)中添加:
 *
 * ```swift
 * dependencies: [
 *     .package(url: "https://github.com/googleads/swift-package-manager-google-mobile-ads", from: "11.13.0"),
 * ],
 * targets: [
 *     .target(
 *         name: "iosApp",
 *         dependencies: [
 *             .product(name: "GoogleMobileAds", package: "swift-package-manager-google-mobile-ads"),
 *         ]
 *     ),
 * ]
 * ```
 *
 * 注意:Google 官方 SwiftPM 仓库地址为
 * `https://github.com/googleads/swift-package-manager-google-mobile-ads`。
 *
 * ### 2. 配置 GADApplicationIdentifier
 *
 * 在 `iosApp/Info.plist` 中添加:
 *
 * ```xml
 * <key>GADApplicationIdentifier</key>
 * <string>ca-app-pub-XXXXX~YYYYY</string>
 * ```
 *
 * ### 3. 在 iosApp 中初始化 AdMob(可选)
 *
 * `iosApp/iosAppApp.swift`(SwiftUI App 入口)中添加:
 *
 * ```swift
 * import GoogleMobileAds
 *
 * @main
 * struct iosAppApp: App {
 *     init() {
 *         GADMobileAds.sharedInstance().start(completionHandler: nil)
 *     }
 *     // ...
 * }
 * ```
 *
 * 或在 Kotlin 侧 `AdManager.initialize(config)` 调用时由 [IosAdManager] 处理
 * (见下方 [IosAdManager] 模板)。
 *
 * ### 4. 启用 [IosAdManager] 真实实现
 *
 * 把 [adPlatformModule] 中的 `NoOpAdManager()` 替换为 `IosAdManager()`。
 * [IosAdManager] 模板见本文件底部(注释块内,默认折叠以避免编译错误)。
 */
actual val adPlatformModule = module {
    // TODO: 启用真实 SDK 后,替换为 IosAdManager()
    single<AdManager> { NoOpAdManager() }
    single<AdBannerRenderer> { IosAdBannerRenderer() }
}

actual val currentPlatform: PlatformType = PlatformType.IOS

private class IosAdBannerRenderer : AdBannerRenderer {
    @Composable
    override fun Render(modifier: Modifier, adUnitId: String) {
        // TODO: 启用后用 UIKitView 渲染 GADBannerView
        //
        // 关键 API:
        // - GADBannerView(adSize: GADAdSizeBanner)  // 横幅
        // - bannerView.adUnitID = adUnitId
        // - bannerView.load(GADRequest())
        // - bannerView.rootViewController = ...
        PlaceholderBanner(
            modifier = modifier,
            label = "iOS Banner (SwiftPM 集成见 docs/feature/ad.md)",
        )
    }
}

// =====================================================================
// 真实接入模板 — 取消下方注释即可使用
// =====================================================================

///**
// * 真实 iOS AdMob 实现
// *
// * 关键 API:
// * - MobileAds.sharedInstance().start { _ in ... } — SDK 初始化
// * - GADBannerView(adSize: GADAdSizeBanner) — 横幅
// * - GADInterstitialAd.load(withAdUnitID:request:completionHandler:)
// * - GADRewardedAd.load(withAdUnitID:request:completionHandler:)
// * - GADFullScreenContentDelegate — 监听广告事件
// *
// * 引用方式:在 iosMain 中通过 import GoogleMobileAds / GADBannerView 等
// * 暴露给 Kotlin 调用的 Swift API。
// */
//class IosAdManager : BaseAdManager() {
//
//    override suspend fun initialize(config: AdUnitConfig) {
//        // 注意: GADApplicationIdentifier 必须在 Info.plist 中预先配置
//        suspendCancellableCoroutine<Unit> { cont ->
//            MobileAds.sharedInstance().start { _ ->
//                if (cont.isActive) cont.resume(Unit)
//            }
//        }
//    }
//
//    override suspend fun preload(type: AdType) {
//        when (type) {
//            AdType.INTERSTITIAL -> loadInterstitial()
//            AdType.REWARDED -> loadRewarded()
//            AdType.BANNER -> Unit
//        }
//    }
//
//    override suspend fun show(type: AdType): Boolean = when (type) {
//        AdType.BANNER -> {
//            tryEmit(AdEvent.ShowFailed(type, "Banner ads are rendered via AdBanner composable"))
//            false
//        }
//        AdType.INTERSTITIAL -> showInterstitial()
//        AdType.REWARDED -> showRewarded()
//    }
//
//    private fun loadInterstitial() {
//        GADInterstitialAd.load(
//            adUnitID = currentConfig.idFor(AdType.INTERSTITIAL),
//            request = GADRequest(),
//        ) { ad, error ->
//            if (ad != null) {
//                interstitialAd = ad
//                ad.fullScreenContentDelegate = IosFullScreenContentDelegate(AdType.INTERSTITIAL, this)
//                tryEmit(AdEvent.Loaded(AdType.INTERSTITIAL))
//            } else {
//                tryEmit(AdEvent.LoadFailed(AdType.INTERSTITIAL, error?.localizedDescription ?: "load failed"))
//            }
//        }
//    }
//
//    private fun showInterstitial(): Boolean {
//        val ad = interstitialAd ?: run {
//            tryEmit(AdEvent.LoadFailed(AdType.INTERSTITIAL, "not loaded"))
//            return false
//        }
//        ad.present(fromRootViewController = rootViewController())
//        return true
//    }
//
//    private fun loadRewarded() { ... }
//    private fun showRewarded(): Boolean { ... }
//
//    @Volatile private var currentConfig: AdUnitConfig = AdUnitConfig.TEST
//    @Volatile private var interstitialAd: GADInterstitialAd? = null
//    @Volatile private var rewardedAd: GADRewardedAd? = null
//
//    private fun rootViewController(): UIViewController = ...
//}
//
//private class IosFullScreenContentDelegate(
//    private val type: AdType,
//    private val manager: IosAdManager,
//) : NSObject(), GADFullScreenContentDelegateProtocol {
//    override fun adDidPresentFullScreenContent(ad: GADFullScreenPresentingAd) {
//        manager.tryEmit(AdEvent.Shown(type))
//    }
//    override fun adDidDismissFullScreenContent(ad: GADFullScreenPresentingAd) {
//        manager.tryEmit(AdEvent.Dismissed(type))
//    }
//    override fun adDidFailToPresentFullScreenContent(ad: GADFullScreenPresentingAd, error: NSError) {
//        manager.tryEmit(AdEvent.ShowFailed(type, error.localizedDescription))
//    }
//}
