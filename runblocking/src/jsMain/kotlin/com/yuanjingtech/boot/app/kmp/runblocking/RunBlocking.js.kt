package com.yuanjingtech.boot.app.kmp.runblocking

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

/** JS `runBlocking` implementation */
actual fun <T> runBlocking(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T,
): T = GlobalScope.promise(context) { block() } as T