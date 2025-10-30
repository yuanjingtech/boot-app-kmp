package com.yuanjingtech.boot.app.kmp.logging.kotlinlogging

// this part should be configured only once in the app to use native android logging
object Static {
    init {
        System.setProperty("kotlin-logging-to-android-native", "true")
    }
}
private val static = Static