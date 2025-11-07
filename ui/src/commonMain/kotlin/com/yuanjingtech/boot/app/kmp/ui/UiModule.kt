package com.yuanjingtech.boot.app.kmp.ui

import com.yuanjingtech.boot.app.kmp.network.networkModule
import org.koin.dsl.module

val uiModule = module {
    includes(networkModule)
}