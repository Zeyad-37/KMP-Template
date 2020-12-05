package com.remote_first.shared.flow_redux

interface Reducer<S : State, R : Result> {
    fun reduce(state: S, result: R): S
}
