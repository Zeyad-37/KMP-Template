package com.remote_first.shared.event_bus

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject
import kotlin.reflect.KClass

class EventBusService @Inject constructor(private val eventBus: EventBus) {

    suspend fun post(t: Any) = eventBus.produceEvent(t)

    fun observe(vararg any: KClass<*>): Flow<Any> = eventBus.events
            .filter { event -> any.any { it == event::class } }
}
