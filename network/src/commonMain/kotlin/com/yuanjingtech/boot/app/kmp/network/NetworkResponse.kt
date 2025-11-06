package com.yuanjingtech.boot.app.kmp.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

class NetworkResponse(internal val httpResponse: HttpResponse)

suspend fun <T> NetworkResponse.body(typeInfo: TypeInfo): T = httpResponse.body(typeInfo) as T

suspend inline fun <reified T> NetworkResponse.body(): T {
    return this.body(typeInfo<T>()) as T
}