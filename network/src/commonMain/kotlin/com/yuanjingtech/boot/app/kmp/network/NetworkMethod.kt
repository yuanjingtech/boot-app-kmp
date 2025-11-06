package com.yuanjingtech.boot.app.kmp.network

/**
 * Represents an HTTP method (verb)
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.http.HttpMethod)
 *
 * @property value contains method name
 */
data class NetworkMethod(val value: String) {
    override fun toString(): String = value

    @Suppress("KDocMissingDocumentation")
    companion object {
        val Get: NetworkMethod = NetworkMethod("GET")
        val Post: NetworkMethod = NetworkMethod("POST")
        val Put: NetworkMethod = NetworkMethod("PUT")

        // https://tools.ietf.org/html/rfc5789
        val Patch: NetworkMethod = NetworkMethod("PATCH")
        val Delete: NetworkMethod = NetworkMethod("DELETE")
        val Head: NetworkMethod = NetworkMethod("HEAD")
        val Options: NetworkMethod = NetworkMethod("OPTIONS")

        /**
         * Parse HTTP method by [method] string
         *
         * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.http.HttpMethod.Companion.parse)
         */
        fun parse(method: String): NetworkMethod {
            return when (method) {
                Get.value -> Get
                Post.value -> Post
                Put.value -> Put
                Patch.value -> Patch
                Delete.value -> Delete
                Head.value -> Head
                Options.value -> Options
                else -> NetworkMethod(method)
            }
        }

        /**
         * A list of default HTTP methods
         *
         * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.http.HttpMethod.Companion.DefaultMethods)
         */
        val DefaultMethods: List<NetworkMethod> = listOf(Get, Post, Put, Patch, Delete, Head, Options)
    }
}