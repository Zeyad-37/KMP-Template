package com.remote_first.shared.flow_redux

import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
fun <T> ConflatedBroadcastChannel<T>.wrap(): CFlow<T> = CFlow(asFlow())

fun <T> Flow<T>.wrap(): CFlow<T> = CFlow(this)

/**
 * An iOS compatible wrapper for Flow (including StateFlow). See
 * * https://github.com/JetBrains/kotlinconf-app/blob/master/common/src/iosMain/kotlin/org/jetbrains/kotlinconf/DispatcherNative.kt
 * * https://github.com/Kotlin/kotlinx.coroutines/issues/462
 * * https://github.com/Kotlin/kotlinx.coroutines/issues/470
 *
 * and an example declaration in Kotlin native and usage from Swift here:
 *
 * * https://github.com/JetBrains/kotlinconf-app/blob/33f2d4e65f470d1444c5d4b46249af8feb243d03/common/src/mobileMain/kotlin/org/jetbrains/kotlinconf/ConferenceService.kt#L278
 * * https://github.com/JetBrains/kotlinconf-app/blob/33f2d4e65f470d1444c5d4b46249af8feb243d03/iosApp/iosApp/Components/SessionCardView.swift#L63-L65
 * * https://github.com/JetBrains/kotlinconf-app/blob/33f2d4e65f470d1444c5d4b46249af8feb243d03/iosApp/iosApp/Components/SessionCardView.swift#L193
 */
class CFlow<T>(private val origin: Flow<T>) : Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()
        onEach {
            block(it)
        }.launchIn(CoroutineScope(Dispatchers.Default + job))
        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}
