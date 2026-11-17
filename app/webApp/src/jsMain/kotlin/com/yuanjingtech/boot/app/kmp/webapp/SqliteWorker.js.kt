package com.yuanjingtech.boot.app.kmp.webapp

import org.w3c.dom.Worker

/**
 * Kotlin/JS half of [newSqliteWorker]; see the `webMain` declaration for why the URL is a
 * literal. `org.w3c.dom.Worker` does expose a `Worker(scriptURL)` constructor here, so the
 * URL is built by JavaScript and handed to that constructor — the shape this target used
 * before the worker was moved out of `:shared`.
 */
@Suppress("UnsafeCastFromDynamic")
internal actual fun newSqliteWorker(): Worker =
    Worker(js("""new URL("sqlite-wasm-worker/worker.js", import.meta.url)"""))