package com.yuanjingtech.boot.app.kmp.subapp

import org.koin.dsl.module

val subAppModule = module {
    single { SubAppManager(getAll()) }
}