package com.yuanjingtech.boot.app.kmp.data.room3

actual fun createBootDatabase(): BootDatabase {
    throw UnsupportedOperationException(
        "Room3 BundledSQLiteDriver is not supported on iOS. " +
            "Use SQLDelight for iOS database operations."
    )
}
