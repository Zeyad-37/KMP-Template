package com.remote_first.androidApp

import app.cash.turbine.Event
import app.cash.turbine.test
import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Error
import com.remote_first.shared.flow_redux.FlowViewModel
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Progress
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.time.Duration

@FlowPreview
fun <I : Input, R : Result, S : State, E : Effect, VM : FlowViewModel<I, R, S, E>> VM.initWith(initialState: S): VM =
        apply { bind(initialState) }

@FlowPreview
suspend fun <I : Input, R : Result, S : State, E : Effect, VM : FlowViewModel<I, R, S, E>> VM.verifyThat(
        offerings: VM.() -> Unit,
): Actual = observeEmissions(offerings)

@FlowPreview
private suspend fun <I : Input, R : Result, S : State, E : Effect, VM : FlowViewModel<I, R, S, E>> VM.observeEmissions(
        offerings: VM.() -> Unit,
): Actual {
    val actualStates = mutableListOf<State>()
    val actualEffects = mutableListOf<Effect>()
    val actualProgress = mutableListOf<Progress>()
    val actualErrors = mutableListOf<Error>()
    observe().test {
        offerings(this@observeEmissions)
        while (true) {
            try {
                when (val event = expectEvent()) {
                    Event.Complete -> cancel()
                    is Event.Error -> throw event.throwable
                    is Event.Item -> when (val item = event.value) {
                        is State -> actualStates += item
                        is Effect -> actualEffects += item
                        is Error -> actualErrors += item
                        is Progress -> actualProgress += item
                    }
                }
            } catch (e: TimeoutCancellationException) {
                break
            }
        }
    }
    return Actual(actualStates, actualEffects, actualProgress, actualErrors)
}

data class Actual(
        val states: List<State>,
        val effects: List<Effect>,
        val progress: List<Progress>,
        val errors: List<Error>,
)

inline fun <reified S : State> Actual.emitsStates(vararg state: S): Actual =
        also { assertTrue(states == (state.toList())) }

inline fun <reified E : Effect> Actual.emitsEffects(vararg effect: E): Actual =
        also { assertTrue(effects == (effect.toList())) }

fun Actual.emitsNoErrors(): Actual = also { assertTrue(errors.isEmpty()) }

fun Actual.emitsNoEffects(): Actual = also { assertTrue(effects.isEmpty()) }