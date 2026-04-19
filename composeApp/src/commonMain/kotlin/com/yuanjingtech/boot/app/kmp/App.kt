package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.includes
import org.koin.plugin.module.dsl.koinConfiguration

@Composable
@Preview
fun App() {
    BootApplication(config = KoinConfiguration {
        printLogger()
        includes(koinConfiguration<BootApp>())
    }) {
        Content(Modifier.fillMaxSize())
    }
}