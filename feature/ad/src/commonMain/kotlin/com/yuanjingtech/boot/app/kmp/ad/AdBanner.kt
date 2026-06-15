package com.yuanjingtech.boot.app.kmp.ad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ad.preview.AdDarkPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ad.preview.AdLightPreviewWrapper
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.koinInject

/**
 * 横幅广告
 *
 * 通过传入完整 [AdUnitConfig] 支持多平台 — 库内部根据 [currentPlatform] 选择
 * 当前平台的广告位 ID。
 *
 * @param config 多平台广告位配置(同时持有 Android / iOS ID)
 * @param modifier 修饰符
 * @param overrideAdUnitId 可选,显式覆盖当前平台的广告位 ID(优先于 config)
 */
@Composable
fun AdBanner(
    config: AdUnitConfig,
    modifier: Modifier = Modifier,
    overrideAdUnitId: String? = null,
) {
    val renderer: AdBannerRenderer = koinInject()
    val resolvedId = overrideAdUnitId?.takeIf { it.isNotEmpty() }
        ?: config.idFor(AdType.BANNER)

    if (resolvedId.isEmpty()) {
        PlaceholderBanner(modifier = modifier, label = "Ad Banner (no unit id for ${currentPlatform})")
        return
    }

    renderer.Render(modifier = modifier.fillMaxWidth(), adUnitId = resolvedId)
}

@Composable
internal fun PlaceholderBanner(
    modifier: Modifier = Modifier,
    label: String = "Ad Placeholder",
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "AdBanner - empty id", group = "feature/ad")
@Composable
@PreviewWrapper(wrapper = AdDarkPreviewWrapper::class)
private fun AdBannerEmptyIdPreview() {
    KoinApplicationPreview(application = { modules(adModule) }) {
        val emptyConfig = AdUnitConfig(
            appIdAndroid = "", appIdIos = "",
            bannerAndroid = "", bannerIos = "",
            interstitialAndroid = "", interstitialIos = "",
            rewardedAndroid = "", rewardedIos = "",
        )
        AdBanner(config = emptyConfig)
    }
}

@Preview(name = "AdBanner - test config (dark)", group = "feature/ad")
@Composable
@PreviewWrapper(wrapper = AdDarkPreviewWrapper::class)
private fun AdBannerDarkPreview() {
    KoinApplicationPreview(application = { modules(adModule) }) {
        AdBanner(config = AdUnitConfig.TEST)
    }
}

@Preview(name = "AdBanner - test config (light)", group = "feature/ad")
@Composable
@PreviewWrapper(wrapper = AdLightPreviewWrapper::class)
private fun AdBannerLightPreview() {
    KoinApplicationPreview(application = { modules(adModule) }) {
        AdBanner(config = AdUnitConfig.TEST)
    }
}

@Preview(name = "AdBanner - override id", group = "feature/ad")
@Composable
@PreviewWrapper(wrapper = AdLightPreviewWrapper::class)
private fun AdBannerOverrideIdPreview() {
    KoinApplicationPreview(application = { modules(adModule) }) {
        AdBanner(
            config = AdUnitConfig.TEST,
            overrideAdUnitId = "ca-app-pub-XXXXXXXX/YYYYYYYY",
        )
    }
}

@Preview(name = "AdBanner in layout", group = "feature/ad")
@Composable
@PreviewWrapper(wrapper = AdLightPreviewWrapper::class)
private fun AdBannerInLayoutPreview() {
    KoinApplicationPreview(application = { modules(adModule) }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Article Title",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                )
                Text(
                    text = "Article body content. The banner is rendered below.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                AdBanner(
                    config = AdUnitConfig.TEST,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }
    }
}
