package com.yuanjingtech.boot.app.kmp.demo

import com.yuanjingtech.boot.app.kmp.demo.subapp.DemoSubApp
import com.yuanjingtech.boot.app.kmp.subapp.SubApp
import org.koin.dsl.bind
import org.koin.dsl.module

val demoModule = module {
    single { DemoSubApp() } bind SubApp::class
}