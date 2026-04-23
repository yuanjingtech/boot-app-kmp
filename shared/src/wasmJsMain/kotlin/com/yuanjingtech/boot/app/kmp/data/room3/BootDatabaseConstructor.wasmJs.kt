package com.yuanjingtech.boot.app.kmp.data.room3

import androidx.room3.RoomDatabaseConstructor

actual object BootDatabaseConstructor : RoomDatabaseConstructor<BootDatabase> {
    actual override fun initialize(): BootDatabase {
        throw UnsupportedOperationException(
            "Room3 BundledSQLiteDriver is not supported on WASM. " +
                "Use SQLDelight for WASM database operations."
        )
    }
}
