package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.call.body

/**
 * 封装的网络响应类，完全隐藏 Ktor HttpResponse 实现
 */
class NetworkResponse internal constructor(
    private val httpResponse: io.ktor.client.statement.HttpResponse
) {
    val status: NetworkStatusCode
        get() = NetworkStatusCode(httpResponse.status.value, httpResponse.status.description)

    val headers: NetworkHeaders
        get() = NetworkHeaders(httpResponse.headers)

    fun isSuccessful(): Boolean = httpResponse.status.value in 200..299

}

suspend fun <T> NetworkResponse.body(typeInfo: TypeInfo): T = httpResponse.body(typeInfo) as T

suspend inline fun <reified T> NetworkResponse.body(): T {
    return this.body(typeInfo<T>()) as T
}

/**
 * 封装的 HTTP 状态码类
 */
data class NetworkStatusCode(
    val value: Int,
    val description: String
)

/**
 * 封装的 HTTP 头部类
 */
class NetworkHeaders internal constructor(
    private val headers: io.ktor.http.Headers
) {
    operator fun get(name: String): String? = headers[name]

    fun getAll(name: String): List<String>? = headers.getAll(name)

    fun entries(): Set<Map.Entry<String, List<String>>> = headers.entries()

    fun names(): Set<String> = headers.names()
}