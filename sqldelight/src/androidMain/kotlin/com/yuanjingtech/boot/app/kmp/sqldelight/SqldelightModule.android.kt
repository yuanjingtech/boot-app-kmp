package com.yuanjingtech.boot.app.kmp.sqldelight

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<DriverFactory> { DriverFactory(androidContext()) }
}