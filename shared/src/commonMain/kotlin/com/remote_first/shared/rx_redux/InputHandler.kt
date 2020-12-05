package com.remote_first.shared.rx_redux

import com.badoo.reaktive.observable.Observable
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.RxOutcome
import com.remote_first.shared.flow_redux.State

interface InputHandler<I : Input, S : State> {
    fun handleInputs(input: I, state: S): Observable<RxOutcome>
}
