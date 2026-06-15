package com.yuanjingtech.boot.app.kmp.ad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.dsl.module

/**
 * Desktop/JVM 平台占位实现
 *
 * AdMob 不支持桌面平台,使用 [NoOpAdManager]。
 */
actual val adPlatformModule = module {
    single<AdManager> { NoOpAdManager() }
    single<AdBannerRenderer> { JvmAdBannerRenderer() }
}

actual val currentPlatform: PlatformType = PlatformType.OTHER

private class JvmAdBannerRenderer : AdBannerRenderer {
    @Composable
    override fun Render(modifier: Modifier, adUnitId: String) {
        PlaceholderBanner(
            modifier = modifier,
            label = "Ad Banner (Desktop 暂不支持)",
        )
    }
}
