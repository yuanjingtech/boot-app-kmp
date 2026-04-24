package com.yuanjingtech.boot.app.kmp.data

import com.yuanjingtech.boot.app.kmp.bootPlatformModule
import com.yuanjingtech.boot.app.kmp.data.room3.BootDatabase
import com.yuanjingtech.boot.app.kmp.data.theme.BootThemeStore
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

internal val bootDataModule = module {
    includes(bootPlatformModule)
    factory { get<BootDatabase>().themeDao() }
    single<BootThemeStore>()
}