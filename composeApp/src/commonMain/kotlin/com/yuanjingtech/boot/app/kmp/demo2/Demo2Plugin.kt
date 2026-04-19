package com.yuanjingtech.boot.app.kmp.demo2

import com.yuanjingtech.boot.app.kmp.plugin.Plugin
import dev.whyoleg.sweetspi.ServiceProvider

@ServiceProvider
object Demo2Plugin : Plugin {
    @Suppress("unused")
    override val module get() = demo2Module
}