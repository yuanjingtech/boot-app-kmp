package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.HttpStatement
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class NetworkServiceKtorImpl(
    private val httpClient: HttpClient,
    private val buildConfig: NetworkBuildConfig,
) : NetworkService {
    override suspend fun get(block: NetworkRequestBuilder.() -> Unit): NetworkResponse {
        val builder = newBuilder().apply {
            block()
        }
        builder.method = NetworkMethod.Get
        return NetworkResponse(request(builder))
    }

    override suspend fun post(block: NetworkRequestBuilder.() -> Unit): NetworkResponse {
        val builder = newBuilder()
        builder.method = NetworkMethod.Post
        builder.block()

        return NetworkResponse(request(builder))
    }

    private suspend inline fun request(
        builder: NetworkRequestBuilder = newBuilder()
    ) = HttpStatement(builder.build(), httpClient).execute()

    /**
     * 按当前构建配置创建请求构造器。
     *
     * debug 标志通过属性设置而非构造参数 —— 保持 `NetworkRequestBuilder`
     * 的 public 构造签名不变（已发布库的二进制兼容性，见该类 KDoc）。
     */
    private fun newBuilder(): NetworkRequestBuilder = NetworkRequestBuilder().apply {
        isDebug = buildConfig.isDebug
    }
}

// createHttpClient() 已按平台拆分到 HttpClientFactory.{platform}.kt（iOS 用 Darwin，其余沿用 CIO）