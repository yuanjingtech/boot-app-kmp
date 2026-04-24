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