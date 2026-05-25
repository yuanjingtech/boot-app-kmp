package com.yuanjingtech.boot.app.kmp.network

import org.junit.Assert.*
import org.junit.Test

class NetworkRequestBuilderTest {

    @Test
    fun networkRequestBuilder_createsValidRequest() {
        val builder = NetworkRequestBuilder()
        builder.url("https://api.example.com/endpoint")

        val request = builder.build()
        assertNotNull(request.url.toString())
        assertTrue(request.url.toString().contains("api.example.com"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun networkRequestBuilder_rejectsLocalhost() {
        val builder = NetworkRequestBuilder()
        builder.url("http://localhost:8080/api")
    }

    @Test(expected = IllegalArgumentException::class)
    fun networkRequestBuilder_rejectsPrivateIP_10() {
        val builder = NetworkRequestBuilder()
        builder.url("http://10.0.0.1/api")
    }

    @Test(expected = IllegalArgumentException::class)
    fun networkRequestBuilder_rejectsPrivateIP_192() {
        val builder = NetworkRequestBuilder()
        builder.url("http://192.168.1.1/api")
    }

    @Test(expected = IllegalArgumentException::class)
    fun networkRequestBuilder_rejectsPrivateIP_172() {
        val builder = NetworkRequestBuilder()
        builder.url("http://172.16.0.1/api")
    }

    @Test
    fun networkRequestBuilder_acceptsValidHttpsUrl() {
        val builder = NetworkRequestBuilder()
        builder.url("https://api.example.com/users")

        val request = builder.build()
        assertTrue(request.url.toString().startsWith("https://"))
    }

    @Test
    fun networkRequestBuilder_setsMethodCorrectly() {
        val builder = NetworkRequestBuilder()
        builder.url("https://api.example.com/endpoint")

        builder.method = NetworkMethod.Get
        assertEquals(NetworkMethod.Get, builder.method)

        builder.method = NetworkMethod.Post
        assertEquals(NetworkMethod.Post, builder.method)
    }
}