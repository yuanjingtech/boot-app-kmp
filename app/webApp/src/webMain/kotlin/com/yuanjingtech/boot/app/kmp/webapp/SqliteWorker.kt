package com.yuanjingtech.boot.app.kmp.webapp

import com.yuanjingtech.boot.app.kmp.BootSqliteWorker
import org.w3c.dom.Worker

/**
 * Supplies the SQLite web worker for this application.
 *
 * `:shared` deliberately ships no worker URL of its own: the URL has to be written as
 * `new URL("…", import.meta.url)` so that webpack resolves it and emits the worker chunk,
 * and that literal would be baked into the published klib — where the consumer's webpack
 * would try to resolve `sqlite-wasm-worker/worker.js`, a package that was never published.
 * The same applies to npm dependencies, which do not travel with the Maven publication.
 *
 * Keeping the literal here works because webpack bundles this module's JavaScript together
 * with the two npm entries declared in `app/webApp/build.gradle.kts`.
 *
 * The expression itself lives in [newSqliteWorker] instead of in the `install` lambda: on
 * Kotlin/Wasm a `js(...)` call must be the single expression of a top-level function body,
 * so it cannot be nested in a lambda.
 */
internal fun installSqliteWorker() {
    BootSqliteWorker.install { newSqliteWorker() }
}

/**
 * Creates the worker that runs SQLite off the main thread.
 *
 * The body has to stay a single `new Worker(new URL("…", import.meta.url))` expression:
 * that is the pattern webpack analyses when it emits the worker chunk. A plain
 * `new Worker("path")` leaves a runtime URL that cannot be resolved, and an
 * `external fun createWorker()` only ever worked if some script defined that global — see
 * `docs/solutions/runtime-errors/webapp-web-sqlite-worker-and-opfs-2026-09-12.md`.
 */
internal expect fun newSqliteWorker(): Worker