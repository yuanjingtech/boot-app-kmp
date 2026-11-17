package com.yuanjingtech.boot.app.kmp.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

actual class DriverFactory {
    actual suspend fun createDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>, name: String): SqlDriver {
        TODO(
            "Kotlin/Wasm has no SQL.js driver yet. Implement it on top of " +
                "SqldelightWorker.create() once a wasm-compatible worker build of " +
                "@cashapp/sqldelight-sqljs-worker exists. See " +
                "docs/solutions/build-errors/sqljs-worker-url-not-portable-2026-11-18.md",
        )
    }
}