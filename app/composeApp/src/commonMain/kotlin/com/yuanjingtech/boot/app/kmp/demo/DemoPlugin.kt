package com.yuanjingtech.boot.app.kmp.demo

import com.yuanjingtech.boot.app.kmp.plugin.Plugin
import dev.whyoleg.sweetspi.ServiceProvider

@ServiceProvider
object DemoPlugin: Plugin {
    @Suppress("unused")
    override val module get() = demoModule
}