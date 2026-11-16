package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

// iOS 必须用 Darwin（NSURLSession）：CIO 在 Kotlin/Native 上没有 TLS 实现。
internal actual fun createHttpClient(): HttpClient = HttpClient(Darwin) {
    installSharedConfig()
}
