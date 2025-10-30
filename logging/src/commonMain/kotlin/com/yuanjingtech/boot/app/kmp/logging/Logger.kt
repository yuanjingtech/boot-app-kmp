package com.yuanjingtech.boot.app.kmp.logging

interface Logger {
    /**
     * Return the name of this `Logger` instance.
     *
     * @return name of this logger instance
     */
    val name: String

    /** Lazy add a log message if isTraceEnabled is true */
    fun trace(message: () -> Any?)

    /** Lazy add a log message if isDebugEnabled is true */
    fun debug(message: () -> Any?)

    /** Lazy add a log message if isInfoEnabled is true */
    fun info(message: () -> Any?)

    /** Lazy add a log message if isWarnEnabled is true */
    fun warn(message: () -> Any?)

    /** Lazy add a log message if isErrorEnabled is true */
    fun error(message: () -> Any?)

    /** Lazy add a log message if isTraceEnabled is true */
    fun trace(throwable: Throwable?, message: () -> Any?)

    /** Lazy add a log message if isDebugEnabled is true */
    fun debug(throwable: Throwable?, message: () -> Any?)

    /** Lazy add a log message if isInfoEnabled is true */
    fun info(throwable: Throwable?, message: () -> Any?)

    /** Lazy add a log message if isWarnEnabled is true */
    fun warn(throwable: Throwable?, message: () -> Any?)

    /** Lazy add a log message if isErrorEnabled is true */
    fun error(throwable: Throwable?, message: () -> Any?)

}