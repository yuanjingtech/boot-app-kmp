package com.yuanjingtech.boot.app.kmp

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import com.yuanjingtech.boot.app.kmp.theme.BootAppTheme
import org.koin.compose.KoinApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.dsl.KoinConfiguration

@OptIn(KoinExperimentalAPI::class)
@Composable
fun BootApplication(
    config: KoinConfiguration,
    logLevel: Level = Level.INFO,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit,
) {
    KoinApplication(
        configuration = config,
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