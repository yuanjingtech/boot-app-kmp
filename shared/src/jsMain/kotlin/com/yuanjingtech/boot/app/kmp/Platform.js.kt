package com.yuanjingtech.boot.app.kmp

import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import com.yuanjingtech.boot.app.kmp.data.room3.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module
import org.w3c.dom.Worker

class JsPlatform : Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()
actual val bootPlatformModule: Module = module {
    single {
        getDatabaseBuilder()
            .setDriver(WebWorkerSQLiteDriver(createSQLiteWorker()))
            .setQueryCoroutineContext(kotlinx.coroutines.Dispatchers.Main)
            .build()
    }
}

/**
 * Creates the web worker that runs SQLite off the main thread.
 *
 * The URL has to be written as `new URL("...", import.meta.url)` so that webpack
 * resolves it at build time and emits `sqlite-wasm-worker/worker.js` (together with
 * its `@sqlite.org/sqlite-wasm` dependency) as a separate worker chunk. A plain
 * `new Worker("path")` would leave an unresolvable runtime URL, and declaring
 * `external fun createWorker()` only works if some script happens to define a
 * global `createWorker` — nothing did, which crashed the app with
 * `ReferenceError: createWorker is not defined`.
 */
@Suppress("UnsafeCastFromDynamic")
private fun createSQLiteWorker(): Worker =
    Worker(js("""new URL("sqlite-wasm-worker/worker.js", import.meta.url)"""))