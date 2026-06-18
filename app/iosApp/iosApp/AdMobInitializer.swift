import GoogleMobileAds
import SwiftUI

/// Google Mobile Ads SDK 初始化器
///
/// 在 iOS App 启动时调用 `AdMobInitializer.initialize()` 完成 SDK 初始化,
/// Kotlin 侧的 [AdManager] 实现可在此基础上发起广告加载/展示。
@MainActor
enum AdMobInitializer {
    private static var didInitialize = false

    /// 初始化 SDK(幂等)
    static func initialize() {
        guard !didInitialize else { return }
        didInitialize = true
        GADMobileAds.sharedInstance().start { status in
            // 适配器映射完成,可以开始请求广告
            #if DEBUG
            print("[AdMob] SDK initialized with \(status.adapterStatusesByClassName.count) adapters")
            #endif
        }
    }
}
