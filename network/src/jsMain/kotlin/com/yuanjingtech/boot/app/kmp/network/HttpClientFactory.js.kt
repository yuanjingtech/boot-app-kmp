package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

// JS 沿用 CIO（Ktor 3 的 CIO 支持浏览器目标，行为与旧实现一致）。
internal actual fun createHttpClient(): HttpClient = HttpClient(CIO) {
    installSharedConfig()
}
