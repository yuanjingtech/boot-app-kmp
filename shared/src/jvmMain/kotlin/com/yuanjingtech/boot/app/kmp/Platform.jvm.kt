package com.yuanjingtech.boot.app.kmp

import org.koin.core.module.Module

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual val bootPlatformModule: Module
    get() = TODO("Not yet implemented")