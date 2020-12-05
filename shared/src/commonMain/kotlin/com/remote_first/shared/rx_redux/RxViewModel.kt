package com.remote_first.shared.rx_redux

import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State

expect abstract class RxViewModel<I : Input, R : Result, S : State, E : Effect> : IRxViewModel<I, R, S, E>
