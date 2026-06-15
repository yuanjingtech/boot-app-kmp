package com.yuanjingtech.boot.app.kmp.ad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.dsl.module

/**
 * Web (JS) 平台占位实现
 *
 * AdMob 不支持 Web 平台,使用 [NoOpAdManager]。
 */
actual val adPlatformModule = module {
    single<AdManager> { NoOpAdManager() }
    single<AdBannerRenderer> { WebAdBannerRenderer() }
}

actual val currentPlatform: PlatformType = PlatformType.OTHER

private class WebAdBannerRenderer : AdBannerRenderer {
    @Composable
    override fun Render(modifier: Modifier, adUnitId: String) {
        PlaceholderBanner(
            modifier = modifier,
            label = "Ad Banner (Web 暂不支持)",
        )
    }
}
