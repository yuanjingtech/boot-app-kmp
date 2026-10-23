package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.isSecure

class NetworkRequestBuilder {
    /**
     * 是否处于 debug 构建。
     *
     * `true` 时放宽 SSRF 校验：允许 `localhost` / `127.0.0.1` / 私网段
     * （见 [NetworkBuildConfig]），便于开发时连本机后端。
     *
     * 默认 `false`（**全量拦截**）—— 保持 release 行为与既有调用方不变。
     * 由 [NetworkServiceKtorImpl] 从 Koin 注入实际值。
     *
     * 刻意**没有**做成构造参数：本类是已发布库的 public API，加构造参数会移除
     * JVM 无参构造器，破坏已编译调用方的二进制兼容性。改成内部属性 + 一个
     * internal setter，public 签名完全不变。
     */
    internal var isDebug: Boolean = false

    private val builder: HttpRequestBuilder = HttpRequestBuilder()

    fun url(urlString: String) {
        // SSRF prevention: validate URL before use
        val validatedUrl = validateUrl(urlString)
        builder.url(validatedUrl)
    }

    private fun validateUrl(urlString: String): String {
        val parsed = URLBuilder(urlString)
        val host = parsed.host.lowercase()

        // Block private/internal IP ranges
        val blockedHosts = listOf(
            "localhost", "127.0.0.1", "0.0.0.0",
            "10.", "172.16.", "172.17.", "172.18.", "172.19.",
            "172.20.", "172.21.", "172.22.", "172.23.",
            "172.24.", "172.25.", "172.26.", "172.27.",
            "172.28.", "172.29.", "172.30.", "172.31.",
            "192.168.", "::1", "[::1]"
        )

        // Check exact matches and prefixes
        val isBlocked = blockedHosts.any { blocked ->
            host == blocked || host.startsWith(blocked) || host.endsWith(".$blocked")
        } || host.matches(Regex("^10\\.\\d+\\.\\d+\\.\\d+$")) ||
                host.matches(Regex("^172\\.(1[6-9]|2[0-9]|3[0-1])\\.\\d+\\.\\d+$")) ||
                host.matches(Regex("^192\\.168\\.\\d+\\.\\d+$"))

        // debug 构建放宽私网/localhost 限制（仅限内网地址，协议校验仍然生效）
        if (isBlocked && !isDebug) {
            throw IllegalArgumentException("URL host blocked for security: $host")
        }

        // Only allow http/https schemes
        if (!parsed.protocol.isSecure() && !parsed.protocol.name.lowercase().startsWith("http")) {
            throw IllegalArgumentException("Only HTTP/HTTPS schemes allowed, got: ${parsed.protocol}")
        }

        return urlString
    }

    var method: NetworkMethod
        get() = NetworkMethod.parse(builder.method.value)
        set(value) {
            builder.method = HttpMethod.parse(value.value)
        }

    fun contentType(contentType: NetworkContentType) {
        builder.contentType(contentType.value)
    }

    fun setBody(body: Any) {
        builder.setBody(body)
    }

    fun headers(block: NetworkHeadersBuilder.() -> Unit) {
        val headersBuilder = NetworkHeadersBuilder()
        headersBuilder.block()
        builder.headers {
            headersBuilder.headers.forEach { (key, value) ->
                append(key, value)
            }
        }
    }

    internal fun build(): HttpRequestBuilder {
        return builder
    }
}