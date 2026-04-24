package com.yuanjingtech.boot.app.kmp

import android.os.Build
import androidx.room3.RoomDatabase
import com.yuanjingtech.boot.app.kmp.data.room3.BootDatabase
import com.yuanjingtech.boot.app.kmp.data.room3.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val bootPlatformModule: Module = module {
    single<RoomDatabase.Builder<BootDatabase>> {
        getDatabaseBuilder(get())
    }
}