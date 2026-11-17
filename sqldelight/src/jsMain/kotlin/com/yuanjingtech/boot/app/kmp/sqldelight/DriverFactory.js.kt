package com.yuanjingtech.boot.app.kmp.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.WebWorkerDriver

actual class DriverFactory {
    actual suspend fun createDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>, name: String): SqlDriver {
        // The worker URL is not portable across artifacts: it belongs to the application, which
        // installs it through SqldelightWorker.install { … }. See SqldelightWorker and
        // docs/solutions/build-errors/sqljs-worker-url-not-portable-2026-11-18.md.
        return WebWorkerDriver(SqldelightWorker.create()).also { schema.create(it).await() }
    }
}