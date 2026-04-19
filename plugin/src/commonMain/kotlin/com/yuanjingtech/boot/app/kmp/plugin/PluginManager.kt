package com.yuanjingtech.boot.app.kmp.plugin

import dev.whyoleg.sweetspi.ServiceLoader

internal object PluginManager {
    val plugins: List<Plugin> by lazy { ServiceLoader.load<Plugin>().toList() }
}