package com.yuanjingtech.boot.app.kmp.runblocking

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.runBlocking

/** JVM `runBlocking` implementation */
actual fun <T> runBlocking(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T,
): T = runBlocking(context, block)