package com.yuanjingtech.boot.app.kmp.logging.kotlinlogging

import com.yuanjingtech.boot.app.kmp.logging.Logger
import io.github.oshai.kotlinlogging.KLogger

class LoggerImpl(private val logger: KLogger) : Logger {
    /**
     * Return the name of this `Logger` instance.
     *
     * @return name of this logger instance
     */
    override val name: String get() = logger.name

    /** Lazy add a log message if isTraceEnabled is true */
    override fun trace(message: () -> Any?) = logger.trace(message)

    /** Lazy add a log message if isTraceEnabled is true */
    override fun trace(throwable: Throwable?, message: () -> Any?) = logger.trace(message)

    /** Lazy add a log message if isDebugEnabled is true */
    override fun debug(message: () -> Any?) = logger.debug(message)

    /** Lazy add a log message if isDebugEnabled is true */
    override fun debug(throwable: Throwable?, message: () -> Any?) = logger.debug(throwable, message)

    /** Lazy add a log message if isInfoEnabled is true */
    override fun info(message: () -> Any?) = logger.info(message)

    /** Lazy add a log message if isInfoEnabled is true */
    override fun info(throwable: Throwable?, message: () -> Any?) = logger.info(throwable, message)

    /** Lazy add a log message if isWarnEnabled is true */
    override fun warn(message: () -> Any?) = logger.warn(message)

    /** Lazy add a log message if isWarnEnabled is true */
    override fun warn(throwable: Throwable?, message: () -> Any?) = logger.warn(throwable, message)

    /** Lazy add a log message if isErrorEnabled is true */
    override fun error(message: () -> Any?) = logger.error(message)

    /** Lazy add a log message if isErrorEnabled is true */
    override fun error(throwable: Throwable?, message: () -> Any?) = logger.error(throwable, message)
}