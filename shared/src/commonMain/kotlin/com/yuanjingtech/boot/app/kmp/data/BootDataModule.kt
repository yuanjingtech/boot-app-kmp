package com.yuanjingtech.boot.app.kmp.data

import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.yuanjingtech.boot.app.kmp.data.room3.BootDatabase
import com.yuanjingtech.boot.app.kmp.data.room3.BootDatabaseConstructor
import com.yuanjingtech.boot.app.kmp.data.theme.BootThemeStore
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

internal val bootDataModule = module {
    single<BootDatabase> { BootDatabaseConstructor.initialize() }
    factory { get<BootDatabase>().themeDao() }
    single<BootThemeStore>()
}