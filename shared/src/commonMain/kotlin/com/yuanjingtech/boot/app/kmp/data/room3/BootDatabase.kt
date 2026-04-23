package com.yuanjingtech.boot.app.kmp.data.room3

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeDao
import com.yuanjingtech.boot.app.kmp.data.theme.room3.ThemeSettings

@Database(
    entities = [ThemeSettings::class],
    version = 1,
    exportSchema = true,
)
abstract class BootDatabase : RoomDatabase() {
    abstract fun themeDao(): ThemeDao
}
