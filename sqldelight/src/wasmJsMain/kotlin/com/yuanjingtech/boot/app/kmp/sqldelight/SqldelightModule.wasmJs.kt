package com.yuanjingtech.boot.app.kmp.sqldelight

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<DriverFactory> { DriverFactory() }
}