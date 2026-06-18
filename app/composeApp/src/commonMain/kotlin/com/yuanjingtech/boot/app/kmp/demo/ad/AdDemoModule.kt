package com.yuanjingtech.boot.app.kmp.demo.ad

import com.yuanjingtech.boot.app.kmp.demo.subapp.DemoSubApp
import com.yuanjingtech.boot.app.kmp.subapp.SubApp
import org.koin.dsl.bind
import org.koin.dsl.module

internal val adDemoModule = module {
    includes(com.yuanjingtech.boot.app.kmp.ad.adModule)
    single { AdDemoSubApp() } bind SubApp::class
}
