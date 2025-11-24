package com.yuanjingtech.boot.app.kmp.demo2

import com.yuanjingtech.boot.app.kmp.demo2.subapp.Demo2SubApp
import com.yuanjingtech.boot.app.kmp.di.bootModule
import com.yuanjingtech.boot.app.kmp.subapp.SubApp
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.dsl.bind
import org.koin.dsl.module

internal val demo2Module = module {
    includes(bootModule)
    single { Demo2SubApp() } bind SubApp::class
}

@Module
@Configuration
class Demo2Module {
    val module get() = demo2Module
}