package com.yuanjingtech.boot.app.kmp.data.room3

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase

internal fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<BootDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("boot_database.db")
    return Room
        .databaseBuilder<BootDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )

}