package com.yuanjingtech.boot.app.kmp.sample.plugin

import com.yuanjingtech.boot.app.kmp.di.bootModule
import org.koin.dsl.module

internal val samplePluginModule = module {
    includes(bootModule)
}

