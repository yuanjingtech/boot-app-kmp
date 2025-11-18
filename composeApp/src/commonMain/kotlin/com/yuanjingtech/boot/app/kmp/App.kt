package com.yuanjingtech.boot.app.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuanjingtech.boot.app.kmp.demo.demoModule
import com.yuanjingtech.boot.app.kmp.demo2.demo2Module
import com.yuanjingtech.boot.app.kmp.di.bootModule
import com.yuanjingtech.boot.app.kmp.webview.WebViewDemoScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.dsl.KoinConfiguration
import org.koin.ksp.generated.configurationModules

@Composable
@Preview
fun App() {
    BootApp(config = KoinConfiguration {
        printLogger()
        modules(BootApp.configurationModules)
        modules(bootModule, demoModule, demo2Module)
    }) {
        WebViewDemoScreen(Modifier.fillMaxSize())
    }
}