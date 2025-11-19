package com.yuanjingtech.boot.app.kmp.network

class NetworkHeadersBuilder {
    internal val headers = mutableMapOf<String, String>()
    
    fun append(name: String, value: String) {
        headers[name] = value
    }
    
    operator fun set(name: String, value: String) {
        headers[name] = value
    }
}