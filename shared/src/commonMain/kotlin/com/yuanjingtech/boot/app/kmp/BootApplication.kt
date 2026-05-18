package com.yuanjingtech.boot.app.kmp

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import com.yuanjingtech.boot.app.kmp.di.bootModule
import com.yuanjingtech.boot.app.kmp.plugin.pluginModule
import com.yuanjingtech.boot.app.kmp.theme.BootAppTheme
import org.koin.compose.KoinApplication
import org.koin.compose.KoinApplicationPreview
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.includes

@OptIn(KoinExperimentalAPI::class)
@Composable
fun BootApplication(
    config: KoinConfiguration,
    logLevel: Level = Level.INFO,
    colorScheme: ColorScheme? = null,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit,
) {
    KoinApplication(
        configuration = KoinConfiguration {
            modules(bootModule)
            modules(pluginModule)
            includes(config)
        },
        logLevel = logLevel,
        content = {
            BootAppTheme(
                colorScheme = colorScheme,
                shapes = shapes,
                typography = typography,
                content = content
            )
        })
}

/**
 * BootApplicationPreview — 简化 Compose 预览的 Koin 依赖注入配置。
 *
 * 自动注入 [bootModule] + [pluginModule]，无需手动调用 `modules(...)`。
 * 等价于直接使用 [KoinApplicationPreview] 并传入 bootModule + pluginModule。
 *
 * ```kotlin
 * @Preview
 * @Composable
 * private fun MyScreenPreview() {
 *     BootApplicationPreview {
 *         MyScreen()
 *     }
 * }
 * ```
 *
 * @param content 要预览的 Composable 内容
 * @see KoinApplicationPreview
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
fun BootApplicationPreview(
    content: @Composable () -> Unit,
) {
    KoinApplicationPreview(
        application = {
            modules(bootModule, pluginModule)
        },
        content = {
            BootAppTheme(content = content)
        },
    )
}