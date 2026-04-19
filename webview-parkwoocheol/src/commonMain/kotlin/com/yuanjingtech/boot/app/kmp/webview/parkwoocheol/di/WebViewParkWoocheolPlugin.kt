package com.yuanjingtech.boot.app.kmp.webview.parkwoocheol.di

import com.yuanjingtech.boot.app.kmp.plugin.Plugin
import dev.whyoleg.sweetspi.ServiceProvider

@ServiceProvider
object WebViewParkWoocheolPlugin : Plugin {
    override val module get() = webviewParkWoocheolModule
}