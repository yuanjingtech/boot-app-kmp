package com.yuanjingtech.boot.app.kmp.webapp

import org.w3c.dom.Worker

/**
 * Kotlin/Wasm half of [newSqliteWorker]; see the `webMain` declaration for why the URL is
 * a literal. There is no `Worker(scriptURL)` constructor for `org.w3c.dom.Worker` on this
 * target, so the whole expression is delegated to JavaScript — which also means it has to
 * be the single expression of this top-level function body.
 */
internal actual fun newSqliteWorker(): Worker =
    js("""new Worker(new URL("sqlite-wasm-worker/worker.js", import.meta.url))""")