package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.HttpStatement
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class NetworkServiceKtorImpl(private val httpClient: HttpClient) : NetworkService {
    override suspend fun get(block: NetworkRequestBuilder.() -> Unit): NetworkResponse {
        val builder = NetworkRequestBuilder().apply {
            block()
        }
        builder.method = NetworkMethod.Get
        return NetworkResponse(request(builder))
    }

    override suspend fun post(block: NetworkRequestBuilder.() -> Unit): NetworkResponse {
        val builder = NetworkRequestBuilder()
        builder.method = NetworkMethod.Post
        builder.block()
        builder.method = NetworkMethod.Post

        return NetworkResponse(request(builder))
    }

    private suspend inline fun request(
        builder: NetworkRequestBuilder = NetworkRequestBuilder()
    ) = HttpStatement(builder.build(), httpClient).execute()
}

internal fun createHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            isLenient = true
            coerceInputValues = true
            ignoreUnknownKeys = true
        })
    }
    defaultRequest {
        host = "1.2.3.4"
        port = 8080
    }
}