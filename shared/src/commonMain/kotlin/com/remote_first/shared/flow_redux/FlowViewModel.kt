package com.remote_first.shared.flow_redux

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
expect abstract class FlowViewModel<I : Input, R : Result, S : State, E : Effect> : IFlowViewModel<I, R, S, E>
