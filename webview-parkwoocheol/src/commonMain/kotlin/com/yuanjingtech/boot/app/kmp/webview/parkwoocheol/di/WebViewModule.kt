package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol.di

import com.yuanjingtech.boot.app.kmp.webview.IWebViewScreen
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val webviewParkWoocheolModule = module {
    single { WebViewScreenImpl() } bind IWebViewScreen::class
}

@Module
@Configuration
class WebViewParkWoocheolModule {
    @Suppress("unused")
    val module get() = webviewParkWoocheolModule
}