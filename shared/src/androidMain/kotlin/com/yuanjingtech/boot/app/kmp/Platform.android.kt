package com.yuanjingtech.boot.app.kmp

import android.content.Context
import android.os.Build
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yuanjingtech.boot.app.kmp.data.room3.BootDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val bootPlatformModule: Module = module {
    single<BootDatabase> {
        val appContext = get<Context>().applicationContext
        val dbFile = appContext.getDatabasePath("boot_database.db")
        Room
            .databaseBuilder<BootDatabase>(context = appContext, name = dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}