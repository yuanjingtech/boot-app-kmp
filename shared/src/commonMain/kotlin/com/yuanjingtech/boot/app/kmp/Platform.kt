package com.yuanjingtech.boot.app.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform