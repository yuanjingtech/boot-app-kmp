package com.yuanjingtech.boot.app.kmp.data.room3

import androidx.room3.Room
import androidx.room3.RoomDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal fun getDatabaseBuilder(): RoomDatabase.Builder<BootDatabase> {
    val dbFilePath = documentDirectory() + "/boot_database.db"
    return Room.databaseBuilder<BootDatabase>(name = dbFilePath)
}

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val url = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(url?.path)
}