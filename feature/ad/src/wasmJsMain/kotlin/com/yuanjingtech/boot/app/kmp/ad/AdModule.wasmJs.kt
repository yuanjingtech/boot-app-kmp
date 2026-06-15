package com.yuanjingtech.boot.app.kmp.ad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.dsl.module

/**
 * WasmJS 平台占位实现
 *
 * AdMob 不支持 WasmJS 平台,使用 [NoOpAdManager]。
 */
actual val adPlatformModule = module {
    single<AdManager> { NoOpAdManager() }
    single<AdBannerRenderer> { WasmJsAdBannerRenderer() }
}

actual val currentPlatform: PlatformType = PlatformType.OTHER

private class WasmJsAdBannerRenderer : AdBannerRenderer {
    @Composable
    override fun Render(modifier: Modifier, adUnitId: String) {
        PlaceholderBanner(
            modifier = modifier,
            label = "Ad Banner (WasmJS 暂不支持)",
        )
    }
}
