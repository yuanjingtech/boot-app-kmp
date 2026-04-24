package com.yuanjingtech.boot.app.kmp.data.room3

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual fun createBootDatabase(): BootDatabase {
    error("Android 需要 Context，请使用 createBootDatabase(context)")
}

fun createBootDatabase(context: Context): BootDatabase {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("boot_database.db")
    return Room.databaseBuilder<BootDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver())
        .build()
}