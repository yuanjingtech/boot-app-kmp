package com.yuanjingtech.boot.app.kmp.data.room3

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual fun createBootDatabase(): BootDatabase {
    return Room.databaseBuilder<BootDatabase>(
        name = "boot_database.db",
    ).setDriver(BundledSQLiteDriver())
        .build()
}