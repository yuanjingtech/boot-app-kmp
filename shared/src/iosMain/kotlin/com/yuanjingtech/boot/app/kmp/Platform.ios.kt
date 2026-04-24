package com.yuanjingtech.boot.app.kmp

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yuanjingtech.boot.app.kmp.data.room3.BootDatabase
import com.yuanjingtech.boot.app.kmp.data.room3.getDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
actual val bootPlatformModule: Module = module {
    single<BootDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}