package com.remote_first.shared.flow_redux

import kotlinx.coroutines.flow.Flow

interface InputHandler<I : Input, S : State> {
    fun handleInputs(input: I, state: S): Flow<RxOutcome>
}
