package com.yuanjingtech.boot.app.kmp.sqldelight

import org.w3c.dom.Worker

/**
 * Injection point for the SQL.js web worker used by [DriverFactory] on Kotlin/JS.
 *
 * The worker script belongs to the npm package `@cashapp/sqldelight-sqljs-worker`. Gradle/Maven
 * artifacts carry no npm dependencies, so this module cannot deliver the worker to its consumers.
 * A `new URL("…sqljs.worker.js", import.meta.url)` literal compiled into this module would be
 * baked into the published `sqldelight-js` klib, and every consumer's bundler would then try to
 * resolve a specifier that exists only when that consumer happened to declare the npm package.
 * That is exactly the failure mode of the older `sqlite-wasm-worker` literal in `:shared`
 * (see `docs/solutions/build-errors/sqljs-worker-url-not-portable-2026-11-18.md`).
 *
 * Applications that actually use the SQL.js driver therefore own the worker and install it once,
 * before the first [DriverFactory.createDriver] call:
 *
 * ```kotlin
 * // consumer's jsMain — the literal must stay in the application, not in the library
 * SqldelightWorker.install {
 *     Worker(js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)"""))
 * }
 * ```
 *
 * `webpack` recognises that literal shape and emits the worker chunk next to the application
 * bundle. Consumers must also declare `npm("@cashapp/sqldelight-sqljs-worker", …)` (the worker
 * script) and `npm("sql.js", …)` (its peer dependency) in their own web source set, and make
 * `sql-wasm.wasm` reachable at the web root — the worker loads it with
 * `locateFile: (file) => "/" + file`.
 */
public object SqldelightWorker {
    private var factory: (() -> Worker)? = null

    /** Registers the factory that creates the SQL.js worker. Call once, before any driver. */
    public fun install(factory: () -> Worker) {
        this.factory = factory
    }

    internal fun create(): Worker {
        val factory = factory
            ?: error(
                "No SQL.js worker is installed. Declare npm(\"@cashapp/sqldelight-sqljs-worker\", …) " +
                    "and npm(\"sql.js\", …) in this module's jsMain, then call " +
                    "SqldelightWorker.install { … } before creating a SQLDelight driver. " +
                    "See docs/solutions/build-errors/sqljs-worker-url-not-portable-2026-11-18.md",
            )
        return factory()
    }
}