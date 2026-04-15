package com.yuanjingtech.boot.app.kmp.webview

import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

val webviewParkWoocheolModule = org.koin.dsl.module {
    single<IWebViewScreen> { WebViewScreenImpl() }
}

@Module
@Configuration
class WebViewParkWoocheolModule {
    @Suppress("unused")
    val module get() = webviewParkWoocheolModule
}