package com.yuanjingtech.boot.app.kmp.ad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 平台 Compose 渲染器
 *
 * 负责将平台原生广告视图嵌入到 Compose 树中。
 * Android 平台使用 AndroidView;iOS 平台使用 UIKitView;其他平台为 no-op。
 */
interface AdBannerRenderer {
    @Composable
    fun Render(
        modifier: Modifier,
        adUnitId: String,
    )
}
