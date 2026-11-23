package com.yuanjingtech.boot.app.kmp.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

actual class DriverFactory {
    actual suspend fun createDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>, name: String): SqlDriver {
        TODO(
            "Kotlin/Wasm has no SQL.js driver yet. Implement it on top of " +
                "SqldelightWorker.create() once a wasm-compatible worker build of " +
                "@cashapp/sqldelight-sqljs-worker exists. " +
                "Note: SqldelightWorker lives in jsMain only (this module has no " +
                "webMain source set), and in Kotlin/Wasm js(...) must be a single " +
                "expression in a top-level function body or property initializer, " +
                "so the js recipe cannot be copied verbatim. See " +
                "docs/solutions/build-errors/sqljs-worker-url-not-portable-2026-11-18.md",
        )
    }
}