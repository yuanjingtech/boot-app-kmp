package com.yuanjingtech.boot.app.kmp

import android.os.Build
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yuanjingtech.boot.app.kmp.data.room3.BootDatabase
import com.yuanjingtech.boot.app.kmp.data.room3.getDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val bootPlatformModule: Module = module {
    single<BootDatabase> {
        getDatabaseBuilder(get())
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}