package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol.di

import com.yuanjingtech.boot.app.kmp.webview.IWebViewScreen
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

internal val webviewParkWoocheolModule = module {
    single<WebViewScreenImpl>() bind IWebViewScreen::class
}
