package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

// JVM 沿用 CIO（行为与旧实现一致）。
internal actual fun createHttpClient(): HttpClient = HttpClient(CIO) {
    installSharedConfig()
}
