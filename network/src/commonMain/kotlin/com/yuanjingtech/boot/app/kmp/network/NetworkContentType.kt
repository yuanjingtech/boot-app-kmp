package com.yuanjingtech.boot.app.kmp.network

import io.ktor.http.ContentType

class NetworkContentType(internal val value: ContentType) {
    object Application {
        val Json = NetworkContentType(ContentType.Application.Json)
    }
}