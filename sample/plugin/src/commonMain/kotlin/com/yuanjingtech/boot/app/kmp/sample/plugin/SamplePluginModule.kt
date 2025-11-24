package com.yuanjingtech.boot.app.kmp.sample.plugin

import com.yuanjingtech.boot.app.kmp.di.bootModule
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.dsl.module

internal val samplePluginModule = module {
    includes(bootModule)
}

@Suppress("unused")
@Module
@Configuration
class SamplePluginModule {
    val module get() = samplePluginModule
}
