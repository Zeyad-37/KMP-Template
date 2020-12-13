package com.remote_first.androidApp

import com.nhaarman.mockitokotlin2.mock
import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Error
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Progress
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import com.remote_first.shared.rx_redux.RxViewModel
import org.junit.jupiter.api.Assertions.assertTrue

fun <I : Input, R : Result, S : State, E : Effect, VM : RxViewModel<I, R, S, E>> VM.initWith(initialState: S): VM =
        apply { bind(initialState) }

fun <I : Input, R : Result, S : State, E : Effect, VM : RxViewModel<I, R, S, E>> VM.verifyThat(
        offerings: VM.() -> Unit,
): Actual = observeEmissions().also { offerings(this) }

private fun <I : Input, R : Result, S : State, E : Effect>
        RxViewModel<I, R, S, E>.observeEmissions(): Actual {
    val actualStates = mutableListOf<State>()
    val actualEffects = mutableListOf<Effect>()
    val actualProgress = mutableListOf<Progress>()
    val actualErrors = mutableListOf<Error>()
    observe(mock()) {
        states { actualStates += it }
        progress { actualProgress += it }
        errors { actualErrors += it }
        effects { actualEffects += it }
    }
    return Actual(actualStates, actualEffects, actualProgress, actualErrors)
}