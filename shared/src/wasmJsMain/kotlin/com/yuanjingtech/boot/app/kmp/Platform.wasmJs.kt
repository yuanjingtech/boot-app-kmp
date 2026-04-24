package com.yuanjingtech.boot.app.kmp

import org.koin.core.module.Module

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
actual val bootPlatformModule: Module
    get() = TODO("Not yet implemented")