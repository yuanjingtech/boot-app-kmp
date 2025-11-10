package com.yuanjingtech.boot.app.kmp.sample.plugin

import com.yuanjingtech.boot.app.kmp.di.bootModule
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.dsl.module

@Module
@Configuration
class SamplePluginModule {
//    val module: org.koin.core.module.Module
//        get() = module {
//            includes(samplePluginModule)
//        }
}

val samplePluginModule = module {
    includes(bootModule)
}