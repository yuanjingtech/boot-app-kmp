package com.yuanjingtech.boot.app.kmp.network

interface NetworkService {
    suspend fun get(block: NetworkRequestBuilder.() -> Unit = {}): NetworkResponse
    suspend fun post(block: NetworkRequestBuilder.() -> Unit = {}): NetworkResponse
}