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
            .setDriver(WebWorkerSQLiteDriver(createWorker()))
            .setQueryCoroutineContext(kotlinx.coroutines.Dispatchers.Main)
            .build()
    }
}

@Suppress("ACTUAL_FUNCTION_WITH_DEFAULT_ARGUMENTS")
external fun createWorker(): Worker