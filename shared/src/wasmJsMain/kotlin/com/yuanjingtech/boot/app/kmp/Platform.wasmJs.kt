package com.yuanjingtech.boot.app.kmp

import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import com.yuanjingtech.boot.app.kmp.data.room3.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
actual val bootPlatformModule: Module = module {
    single {
        getDatabaseBuilder()
            // The worker is supplied by the application, not by this library: a
            // `new URL("...", import.meta.url)` literal here would be baked into the
            // published klib and resolved by the consumer's webpack, which cannot see
            // the unpublished `sqlite-wasm-worker` npm package. See BootSqliteWorker.
            .setDriver(WebWorkerSQLiteDriver(BootSqliteWorker.create()))
            .setQueryCoroutineContext(kotlinx.coroutines.Dispatchers.Main)
            .build()
    }
}