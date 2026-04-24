package com.yuanjingtech.boot.app.kmp

import org.koin.core.module.Module

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()
actual val bootPlatformModule: Module
    get() = TODO("Not yet implemented")