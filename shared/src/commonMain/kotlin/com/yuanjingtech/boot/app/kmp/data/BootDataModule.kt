package com.yuanjingtech.boot.app.kmp.data

import com.yuanjingtech.boot.app.kmp.data.room3.BootDatabase
import com.yuanjingtech.boot.app.kmp.data.room3.createBootDatabase
import com.yuanjingtech.boot.app.kmp.data.theme.BootThemeStore
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

internal val bootDataModule = module {
    single<BootDatabase> { createBootDatabase() }
    single<BootThemeStore>()
}