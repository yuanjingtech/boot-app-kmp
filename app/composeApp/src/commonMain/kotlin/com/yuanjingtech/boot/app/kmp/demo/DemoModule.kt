package com.yuanjingtech.boot.app.kmp.demo

import com.yuanjingtech.boot.app.kmp.demo.subapp.DemoSubApp
import com.yuanjingtech.boot.app.kmp.di.bootModule
import com.yuanjingtech.boot.app.kmp.subapp.SubApp
import org.koin.dsl.bind
import org.koin.dsl.module

internal val demoModule = module {
    includes(bootModule)
    single { DemoSubApp() } bind SubApp::class
}

