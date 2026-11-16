package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * 平台相关的 HttpClient 工厂。
 *
 * 旧实现固定使用 CIO 引擎，而 CIO 的 TLS 在 Kotlin/Native 上没有实现
 * （ktor-network-tls 的 nonJvm 版本直接
 * `error("TLS sessions are not supported on Native platform.")`），
 * 导致 iOS 上任何 HTTPS 请求都会失败。
 *
 * 现按平台选择引擎：
 * - iOS（iosMain）：Darwin（NSURLSession），TLS 由系统实现；
 * - Android / JVM / JS / WasmJs：CIO（与旧实现行为一致）。
 *
 * 共同的 JSON 内容协商配置收敛到 [installSharedConfig]。
 */
internal expect fun createHttpClient(): HttpClient

/**
 * 各平台 actual 共用的 HttpClient 配置（JSON 内容协商）。
 */
internal fun HttpClientConfig<*>.installSharedConfig() {
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            isLenient = true
            coerceInputValues = true
            ignoreUnknownKeys = true
        })
    }
}
