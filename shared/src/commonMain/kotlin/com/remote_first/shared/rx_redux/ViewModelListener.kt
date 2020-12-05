package com.remote_first.shared.rx_redux

import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Progress
import com.remote_first.shared.flow_redux.State
import com.remote_first.shared.flow_redux.Error

interface ViewModelListener<S : State, E : Effect> {
    var effects: (effect: E) -> Unit
    var states: (state: S) -> Unit
    var errors: (error: Error) -> Unit
    var progress: (progress: Progress) -> Unit
}

class ViewModelListenerHelper<S : State, E : Effect> : ViewModelListener<S, E> {
    override var effects: (effect: E) -> Unit = {}
    override var states: (state: S) -> Unit = {}
    override var progress: (progress: Progress) -> Unit = {}
    override var errors: (error: Error) -> Unit = {}

    fun errors(errors: (error: Error) -> Unit) {
        this.errors = errors
    }

    fun effects(effects: (effect: Effect) -> Unit) {
        this.effects = effects
    }

    fun states(states: (state: State) -> Unit) {
        this.states = states
    }

    fun progress(progress: (progress: Progress) -> Unit) {
        this.progress = progress
    }
}
