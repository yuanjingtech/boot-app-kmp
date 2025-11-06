package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class NetworkRequestBuilder {
    private val builder: HttpRequestBuilder = HttpRequestBuilder()
    fun url(urlString: String) {
        builder.url(urlString)
    }

    var method: NetworkMethod
        get() = NetworkMethod.parse(builder.method.value)
        set(value) {
            builder.method = HttpMethod.parse(value.value)
        }

    internal fun build(): HttpRequestBuilder {
        return builder
    }
}