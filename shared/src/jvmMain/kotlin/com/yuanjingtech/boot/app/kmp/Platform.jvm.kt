package com.yuanjingtech.boot.app.kmp

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yuanjingtech.boot.app.kmp.data.room3.BootDatabase
import com.yuanjingtech.boot.app.kmp.data.room3.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual val bootPlatformModule: Module = module {
    single<BootDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(kotlinx.coroutines.Dispatchers.IO)
            .build()
    }
}