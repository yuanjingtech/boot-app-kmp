package com.yuanjingtech.boot.app.kmp.data.room3

actual fun createBootDatabase(): BootDatabase {
    throw UnsupportedOperationException(
        "Room3 WebWorkerSQLiteDriver is not yet implemented on WASM. " +
            "Use SQLDelight for WASM database operations."
    )
}