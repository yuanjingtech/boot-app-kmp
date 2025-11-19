package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

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