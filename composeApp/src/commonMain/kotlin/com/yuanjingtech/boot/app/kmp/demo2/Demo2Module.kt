package com.yuanjingtech.boot.app.kmp.demo2

import com.yuanjingtech.boot.app.kmp.demo2.subapp.Demo2SubApp
import com.yuanjingtech.boot.app.kmp.subapp.SubApp
import org.koin.dsl.bind
import org.koin.dsl.module

val demo2Module = module {
    single { Demo2SubApp() } bind SubApp::class
}