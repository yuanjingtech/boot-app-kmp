package com.yuanjingtech.boot.app.kmp.di

import com.yuanjingtech.boot.app.kmp.network.networkModule
import com.yuanjingtech.boot.app.kmp.sqldelight.sqldelightModule
import com.yuanjingtech.boot.app.kmp.subapp.subAppModule
import com.yuanjingtech.boot.app.kmp.ui.uiModule
import org.koin.dsl.module

val bootModule = module {
    includes(sqldelightModule, subAppModule, networkModule, uiModule)
}