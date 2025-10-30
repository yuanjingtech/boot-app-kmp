package com.yuanjingtech.boot.app.kmp.logging

import com.yuanjingtech.boot.app.kmp.logging.kotlinlogging.LoggerImpl
import io.github.oshai.kotlinlogging.KotlinLogging

object LoggerFactory {
    fun getLogger(name: String): Logger = LoggerImpl(KotlinLogging.logger(name))
}

