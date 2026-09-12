package com.yuanjingtech.boot.app.kmp

import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import com.yuanjingtech.boot.app.kmp.data.room3.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module
import org.w3c.dom.Worker

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
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
 * Unlike the Kotlin/JS target there is no `Worker(scriptURL)` constructor available
 * for `org.w3c.dom.Worker` here, so the whole expression is delegated to JavaScript.
 * It still has to contain `new URL("...", import.meta.url)` literally, because that
 * is the pattern webpack recognizes when emitting the worker chunk — see
 * `Platform.js.kt` for the Kotlin/JS counterpart.
 */
private fun createSQLiteWorker(): Worker =
    js("""new Worker(new URL("sqlite-wasm-worker/worker.js", import.meta.url))""")