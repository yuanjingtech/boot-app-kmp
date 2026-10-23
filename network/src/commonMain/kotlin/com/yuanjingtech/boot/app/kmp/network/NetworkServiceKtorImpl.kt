package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
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
        val builder = NetworkRequestBuilder(isDebug = buildConfig.isDebug).apply {
            block()
        }
        builder.method = NetworkMethod.Get
        return NetworkResponse(request(builder))
    }

    override suspend fun post(block: NetworkRequestBuilder.() -> Unit): NetworkResponse {
        val builder = NetworkRequestBuilder(isDebug = buildConfig.isDebug)
        builder.method = NetworkMethod.Post
        builder.block()

        return NetworkResponse(request(builder))
    }

    private suspend inline fun request(
        builder: NetworkRequestBuilder = NetworkRequestBuilder(isDebug = buildConfig.isDebug)
    ) = HttpStatement(builder.build(), httpClient).execute()
}

internal fun createHttpClient() = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            isLenient = true
            coerceInputValues = true
            ignoreUnknownKeys = true
        })
    }
    // Note: host should be provided per-request via NetworkRequestBuilder.url()
    // DefaultRequest intentionally omits hardcoded host to avoid traffic redirection risk
}