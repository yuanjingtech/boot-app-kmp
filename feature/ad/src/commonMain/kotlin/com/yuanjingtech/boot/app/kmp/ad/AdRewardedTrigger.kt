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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.yuanjingtech.boot.app.kmp.ad.preview.AdDarkPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ad.preview.AdLightPreviewWrapper
import com.yuanjingtech.boot.app.kmp.ad.preview.AdUnitConfigParameterProvider
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.koinInject

/**
 * 激励视频触发器
 *
 * @param config 多平台广告位配置
 * @param trigger 触发器,值变化时尝试展示
 * @param overrideAdUnitId 可选,显式覆盖当前平台广告位 ID
 * @param onReward 用户获得奖励回调
 */
@Composable
fun AdRewardedTrigger(
    config: AdUnitConfig,
    trigger: Any?,
    overrideAdUnitId: String? = null,
    onReward: (AdReward?) -> Unit = {},
) {
    val manager: AdManager = koinInject()
    val event by manager.events.collectAsState(initial = null)

    LaunchedEffect(event, trigger) {
        if (trigger == null) return@LaunchedEffect
        if (event is AdEvent.Rewarded) {
            onReward((event as AdEvent.Rewarded).reward)
        }
    }

    LaunchedEffect(trigger) {
        if (trigger == null) return@LaunchedEffect
        val id = overrideAdUnitId?.takeIf { it.isNotEmpty() } ?: config.idFor(AdType.REWARDED)
        if (id.isEmpty()) return@LaunchedEffect
        manager.show(AdType.REWARDED)
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "AdRewardedTrigger - dark", group = "feature/ad")
@Composable
@PreviewWrapper(wrapper = AdDarkPreviewWrapper::class)
private fun AdRewardedTriggerDarkPreview() {
    KoinApplicationPreview(application = { modules(adModule) }) {
        var coins by remember { mutableIntStateOf(0) }
        var triggerKey by remember { mutableStateOf<Any?>(null) }
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Coins: $coins",
                    style = MaterialTheme.typography.titleLarge,
                )
                Button(
                    onClick = { triggerKey = Any() },
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    Text("Watch rewarded video")
                }
                AdRewardedTrigger(
                    config = AdUnitConfig.TEST,
                    trigger = triggerKey,
                    onReward = { reward -> coins += reward?.amount ?: 0 },
                )
            }
        }
    }
}

@Preview(name = "AdRewardedTrigger - light", group = "feature/ad")
@Composable
@PreviewWrapper(wrapper = AdLightPreviewWrapper::class)
private fun AdRewardedTriggerLightPreview() {
    KoinApplicationPreview(application = { modules(adModule) }) {
        var coins by remember { mutableIntStateOf(0) }
        var triggerKey by remember { mutableStateOf<Any?>(null) }
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Coins: $coins",
                    style = MaterialTheme.typography.titleLarge,
                )
                Button(
                    onClick = { triggerKey = Any() },
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    Text("Watch rewarded video")
                }
                AdRewardedTrigger(
                    config = AdUnitConfig.TEST,
                    trigger = triggerKey,
                    onReward = { reward -> coins += reward?.amount ?: 0 },
                )
            }
        }
    }
}

// ─── PreviewParameter 变体预览 ────────────────────────────────────────────────

@Preview(name = "AdRewardedTrigger per config", group = "feature/ad - variants")
@Composable
@PreviewWrapper(wrapper = AdLightPreviewWrapper::class)
private fun AdRewardedTriggerPerConfigPreview(
    @PreviewParameter(AdUnitConfigParameterProvider::class) config: AdUnitConfig,
) {
    KoinApplicationPreview(application = { modules(adModule) }) {
        var coins by remember { mutableIntStateOf(0) }
        var triggerKey by remember { mutableStateOf<Any?>(null) }
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Coins: $coins",
                    style = MaterialTheme.typography.titleLarge,
                )
                Button(
                    onClick = { triggerKey = Any() },
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    Text("Watch")
                }
                AdRewardedTrigger(
                    config = config,
                    trigger = triggerKey,
                    onReward = { reward -> coins += reward?.amount ?: 0 },
                )
            }
        }
    }
}
