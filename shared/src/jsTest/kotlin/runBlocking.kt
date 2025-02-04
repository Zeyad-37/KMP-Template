package com.example.multiplatform.shared.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.coroutines.CoroutineContext

fun <T> runBlocking(context: CoroutineContext, block: suspend CoroutineScope.() -> T): dynamic =
    GlobalScope.promise(context) { block() }
