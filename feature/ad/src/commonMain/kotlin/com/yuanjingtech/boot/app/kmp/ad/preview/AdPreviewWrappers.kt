package com.yuanjingtech.boot.app.kmp.ad.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider

/**
 * 暗色主题 Preview 包装器
 */
class AdDarkPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        MaterialTheme(colorScheme = darkColorScheme()) {
            content()
        }
    }
}

/**
 * 亮色主题 Preview 包装器
 */
class AdLightPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        MaterialTheme(colorScheme = lightColorScheme()) {
            content()
        }
    }
}
