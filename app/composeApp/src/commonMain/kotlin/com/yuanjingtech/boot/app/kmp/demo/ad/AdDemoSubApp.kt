package com.yuanjingtech.boot.app.kmp.demo.ad

import com.yuanjingtech.boot.app.kmp.ad.AdBanner
import com.yuanjingtech.boot.app.kmp.ad.AdEvent
import com.yuanjingtech.boot.app.kmp.ad.AdInterstitialTrigger
import com.yuanjingtech.boot.app.kmp.ad.AdManager
import com.yuanjingtech.boot.app.kmp.ad.AdRewardedTrigger
import com.yuanjingtech.boot.app.kmp.ad.AdType
import com.yuanjingtech.boot.app.kmp.ad.AdUnitConfig
import com.yuanjingtech.boot.app.kmp.subapp.SubApp
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

/**
 * Ad Demo SubApp — 演示三种广告位的集成。
 */
class AdDemoSubApp : SubApp {
    override val id: String = "ad-demo"
    override val name: String = "广告演示"
    override val description: String = "展示 Banner / Interstitial / Rewarded 三种广告位"

    override fun content(): @Composable() ((modifier: Modifier) -> Unit) = { modifier ->
        AdDemoScreen(modifier)
    }
}

@Composable
fun AdDemoScreen(modifier: Modifier = Modifier) {
    val manager: AdManager = koinInject()
    val productionConfig = remember { sampleProductionConfig() }
    val configProvider = koinInject<com.yuanjingtech.boot.app.kmp.ad.AdUnitConfigProvider>()
    val effectiveConfig = remember(productionConfig) {
        configProvider.current(productionConfig)
    }
    val scope = rememberCoroutineScope()

    // 一次性初始化
    LaunchedEffect(effectiveConfig) {
        manager.initialize(effectiveConfig)
    }

    var interstitialTrigger by remember { mutableIntStateOf(0) }
    var rewardedTrigger by remember { mutableStateOf<Any?>(null) }
    var lastEvent by remember { mutableStateOf<AdEvent?>(null) }
    var coins by remember { mutableIntStateOf(0) }
    val event by manager.events.collectAsState(initial = null)
    LaunchedEffect(event) {
        if (event != null) {
            lastEvent = event
            if (event is AdEvent.Rewarded) {
                coins += (event as AdEvent.Rewarded).reward.amount
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("广告演示", style = MaterialTheme.typography.titleLarge)

            // Banner 区
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Banner", style = MaterialTheme.typography.titleMedium)
                    AdBanner(
                        config = effectiveConfig,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }

            // Interstitial 触发
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Interstitial", style = MaterialTheme.typography.titleMedium)
                    Button(
                        onClick = {
                            scope.launch { manager.preload(AdType.INTERSTITIAL) }
                            interstitialTrigger++
                        },
                        modifier = Modifier.padding(top = 8.dp),
                    ) {
                        Text("加载并展示")
                    }
                    AdInterstitialTrigger(
                        config = effectiveConfig,
                        trigger = interstitialTrigger,
                    )
                }
            }

            // Rewarded 触发
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Rewarded (coins: $coins)", style = MaterialTheme.typography.titleMedium)
                    Button(
                        onClick = {
                            scope.launch { manager.preload(AdType.REWARDED) }
                            rewardedTrigger = System.currentTimeMillis()
                        },
                        modifier = Modifier.padding(top = 8.dp),
                    ) {
                        Text("观看激励视频")
                    }
                    AdRewardedTrigger(
                        config = effectiveConfig,
                        trigger = rewardedTrigger,
                        onReward = { /* 在 events 回调中已累加 */ },
                    )
                }
            }

            // 最近事件
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("最近事件", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = lastEvent?.let { describe(it) } ?: "(无)",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

private fun describe(event: AdEvent): String = when (event) {
    is AdEvent.Loaded -> "Loaded: ${event.type}"
    is AdEvent.LoadFailed -> "LoadFailed: ${event.type} — ${event.message}"
    is AdEvent.Shown -> "Shown: ${event.type}"
    is AdEvent.Clicked -> "Clicked: ${event.type}"
    is AdEvent.Dismissed -> "Dismissed: ${event.type}"
    is AdEvent.Rewarded -> "Rewarded: +${event.reward.amount} ${event.reward.type}"
    is AdEvent.ShowFailed -> "ShowFailed: ${event.type} — ${event.message}"
}

/**
 * 示例生产配置。release 构建中应替换为真实 ID。
 */
private fun sampleProductionConfig(): AdUnitConfig = AdUnitConfig(
    appIdAndroid = "ca-app-pub-XXXXXXXX~YYYYYY",
    appIdIos = "ca-app-pub-XXXXXXXX~ZZZZZZ",
    bannerAndroid = "ca-app-pub-XXXXXXXX/AAAAAAAA",
    bannerIos = "ca-app-pub-XXXXXXXX/BBBBBBBB",
    interstitialAndroid = "ca-app-pub-XXXXXXXX/CCCCCCCC",
    interstitialIos = "ca-app-pub-XXXXXXXX/DDDDDDDD",
    rewardedAndroid = "ca-app-pub-XXXXXXXX/EEEEEEEE",
    rewardedIos = "ca-app-pub-XXXXXXXX/FFFFFFFF",
)
