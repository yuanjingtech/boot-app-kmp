package com.yuanjingtech.boot.app.kmp.ad

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.AdView as GoogleAdView
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.coroutines.resume

actual val adPlatformModule = module {
    single<AdManager> { AndroidAdManager(androidContext()) }
    single<AdBannerRenderer> { AndroidAdBannerRenderer(androidContext()) }
}

actual val currentPlatform: PlatformType = PlatformType.ANDROID

private class AndroidAdManager(
    private val context: Context,
) : BaseAdManager() {

    @Volatile
    private var config: AdUnitConfig = AdUnitConfig.TEST

    @Volatile
    private var interstitialAd: InterstitialAd? = null

    @Volatile
    private var rewardedAd: RewardedAd? = null

    override suspend fun initialize(config: AdUnitConfig) {
        this.config = config
        suspendCancellableCoroutine<Unit> { cont ->
            MobileAds.initialize(context) { cont.resume(Unit) }
        }
    }

    override suspend fun preload(type: AdType) {
        when (type) {
            AdType.INTERSTITIAL -> loadInterstitial()
            AdType.REWARDED -> loadRewarded()
            AdType.BANNER -> Unit
        }
    }

    override suspend fun show(type: AdType): Boolean {
        val activity = context.findActivity() ?: run {
            tryEmit(AdEvent.ShowFailed(type, "No Activity context"))
            return false
        }
        return when (type) {
            AdType.BANNER -> {
                tryEmit(AdEvent.ShowFailed(type, "Banner ads are rendered via AdBanner composable"))
                false
            }
            AdType.INTERSTITIAL -> showInterstitial(activity)
            AdType.REWARDED -> showRewarded(activity)
        }
    }

    private suspend fun loadInterstitial() {
        val loaded = suspendCancellableCoroutine<InterstitialAd?> { cont ->
            InterstitialAd.load(
                context,
                config.idFor(AdType.INTERSTITIAL),
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        if (cont.isActive) cont.resume(ad)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        if (cont.isActive) cont.resume(null)
                    }
                },
            )
        }
        if (loaded != null) {
            interstitialAd = loaded
            tryEmit(AdEvent.Loaded(AdType.INTERSTITIAL))
        } else {
            tryEmit(AdEvent.LoadFailed(AdType.INTERSTITIAL, "Interstitial load failed"))
        }
    }

    private fun showInterstitial(activity: Activity): Boolean {
        val ad = interstitialAd ?: run {
            tryEmit(AdEvent.LoadFailed(AdType.INTERSTITIAL, "Interstitial ad not loaded"))
            return false
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                tryEmit(AdEvent.Shown(AdType.INTERSTITIAL))
            }

            override fun onAdDismissedFullScreenContent() {
                tryEmit(AdEvent.Dismissed(AdType.INTERSTITIAL))
                interstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                tryEmit(AdEvent.ShowFailed(AdType.INTERSTITIAL, error.message))
            }
        }
        ad.show(activity)
        return true
    }

    private suspend fun loadRewarded() {
        val loaded = suspendCancellableCoroutine<RewardedAd?> { cont ->
            RewardedAd.load(
                context,
                config.idFor(AdType.REWARDED),
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        if (cont.isActive) cont.resume(ad)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        if (cont.isActive) cont.resume(null)
                    }
                },
            )
        }
        if (loaded != null) {
            rewardedAd = loaded
            tryEmit(AdEvent.Loaded(AdType.REWARDED))
        } else {
            tryEmit(AdEvent.LoadFailed(AdType.REWARDED, "Rewarded load failed"))
        }
    }

    private fun showRewarded(activity: Activity): Boolean {
        val ad = rewardedAd ?: run {
            tryEmit(AdEvent.LoadFailed(AdType.REWARDED, "Rewarded ad not loaded"))
            return false
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                tryEmit(AdEvent.Shown(AdType.REWARDED))
            }

            override fun onAdDismissedFullScreenContent() {
                tryEmit(AdEvent.Dismissed(AdType.REWARDED))
                rewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                tryEmit(AdEvent.ShowFailed(AdType.REWARDED, error.message))
            }
        }
        ad.show(activity) { reward: RewardItem ->
            tryEmit(AdEvent.Rewarded(AdType.REWARDED, AdReward(reward.type, reward.amount)))
        }
        return true
    }
}

private class AndroidAdBannerRenderer(
    private val context: Context,
) : AdBannerRenderer {
    @Composable
    override fun Render(modifier: Modifier, adUnitId: String) {
        AndroidView(
            modifier = modifier,
            factory = { ctx ->
                GoogleAdView(ctx).apply {
                    setAdSize(AdSize.BANNER)
                    // 通过反射写入,Google AdView SDK 本身提供 setAdUnitId(),Kotlin 仅识别为只读属性
                    javaClass.getMethod("setAdUnitId", String::class.java)
                        .invoke(this, adUnitId)
                    loadAd(AdRequest.Builder().build())
                }
            },
        )
    }
}

private fun Context.findActivity(): Activity? {
    var ctx: Context? = this
    while (ctx is android.content.ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
