package com.yuanjingtech.boot.app.kmp.data.room3

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver

internal fun getDatabaseBuilder(): RoomDatabase.Builder<BootDatabase> {
    return Room.databaseBuilder<BootDatabase>("app_database.db")

}