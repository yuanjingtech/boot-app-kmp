package com.yuanjingtech.boot.app.kmp.ad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
 * 插屏广告触发器(无 UI,业务代码手动调用 [AdManager.show])
 *
 * @param config 多平台广告位配置
 * @param trigger 触发器,值变化时尝试展示
 * @param overrideAdUnitId 可选,显式覆盖当前平台广告位 ID
 * @param onShown 展示完成回调
 */
@Composable
fun AdInterstitialTrigger(
    config: AdUnitConfig,
    trigger: Any?,
    overrideAdUnitId: String? = null,
    onShown: (Boolean) -> Unit = {},
) {
    val manager: AdManager = koinInject()
    val state by manager.events.collectAsState(initial = null)

    LaunchedEffect(trigger) {
        if (trigger == null) return@LaunchedEffect
        // 显式覆盖仅在当前平台生效(若 overrideAdUnitId 非空)
        val id = overrideAdUnitId?.takeIf { it.isNotEmpty() } ?: config.idFor(AdType.INTERSTITIAL)
        if (id.isEmpty()) {
            onShown(false)
            return@LaunchedEffect
        }
        val shown = manager.show(AdType.INTERSTITIAL)
        onShown(shown)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "AdInterstitialTrigger - dark", group = "feature/ad")
@Composable
@PreviewWrapper(wrapper = AdDarkPreviewWrapper::class)
private fun AdInterstitialTriggerDarkPreview() {
    KoinApplicationPreview(application = { modules(adModule) }) {
        var counter by remember { mutableIntStateOf(0) }
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Click counter: $counter",
                    style = MaterialTheme.typography.titleMedium,
                )
                Button(
                    onClick = { counter++ },
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    Text("Trigger interstitial")
                }
                AdInterstitialTrigger(
                    config = AdUnitConfig.TEST,
                    trigger = counter,
                )
            }
        }
    }
}

@Preview(name = "AdInterstitialTrigger - light", group = "feature/ad")
@Composable
@PreviewWrapper(wrapper = AdLightPreviewWrapper::class)
private fun AdInterstitialTriggerLightPreview() {
    KoinApplicationPreview(application = { modules(adModule) }) {
        var counter by remember { mutableIntStateOf(0) }
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Click counter: $counter",
                    style = MaterialTheme.typography.titleMedium,
                )
                Button(
                    onClick = { counter++ },
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    Text("Trigger interstitial")
                }
                AdInterstitialTrigger(
                    config = AdUnitConfig.TEST,
                    trigger = counter,
                )
            }
        }
    }
}
