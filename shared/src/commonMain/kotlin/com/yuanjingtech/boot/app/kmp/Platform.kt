package com.yuanjingtech.boot.app.kmp

import org.koin.core.module.Module
import org.koin.dsl.module

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect val bootPlatformModule: Module
