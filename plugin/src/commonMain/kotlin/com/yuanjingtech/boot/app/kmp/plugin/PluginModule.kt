package com.yuanjingtech.boot.app.kmp.plugin

import org.koin.dsl.module

val pluginModule = module {
    includes(PluginManager.plugins.map { it.module })
}