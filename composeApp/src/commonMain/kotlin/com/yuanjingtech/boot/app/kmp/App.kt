package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yuanjingtech.boot.app.kmp.webview.WebViewDemoScreen
import org.koin.dsl.KoinConfiguration
import org.koin.ksp.generated.configurationModules

@Composable
@Preview
fun App() {
    BootApp(config = KoinConfiguration {
        printLogger()
        modules(BootApp.configurationModules)
    }) {
        WebViewDemoScreen(Modifier.fillMaxSize())
    }
}