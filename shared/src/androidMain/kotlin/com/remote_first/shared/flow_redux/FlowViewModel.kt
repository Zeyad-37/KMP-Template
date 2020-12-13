package com.remote_first.shared.flow_redux

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remote_first.shared.flow_redux.InputStrategy.DEBOUNCE
import com.remote_first.shared.flow_redux.InputStrategy.NONE
import com.remote_first.shared.flow_redux.InputStrategy.THROTTLE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates.observable

@FlowPreview
@ExperimentalCoroutinesApi
actual abstract class FlowViewModel<I : Input, R : Result, S : State, E : Effect>(
        override val inputHandler: InputHandler<I, S>,
        override val reducer: Reducer<S, R>,
        var savedStateHandle: SavedStateHandle?,
        override val computationDispatcher: CoroutineDispatcher = Dispatchers.Default,
        override val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : IFlowViewModel<I, R, S, E>, ViewModel() {

    override lateinit var currentState: S
    override lateinit var scope: CoroutineScope

    override var trackingListener: TrackingListener<I, R, S, E> = this.initTracking()
    override var loggingListener: LoggingListener<I, R, S, E> = this.initLogging()
    override var progress: Progress by observable(Progress(false, EmptyInput),
            { _, oldValue, newValue ->
                if (newValue != oldValue) notifyProgressChanged(newValue)
            })

    override val viewModelListener: MutableStateFlow<Output> by lazy { MutableStateFlow(currentState as Output) }
    override val inputs: MutableSharedFlow<I> = MutableSharedFlow()
    override val throttledInputs: MutableSharedFlow<I> = MutableSharedFlow()
    override val debouncedInputs: MutableSharedFlow<I> = MutableSharedFlow()

    override fun bind(initialState: S, inputs: () -> MutableSharedFlow<I>): FlowViewModel<I, R, S, E> {
        currentState = savedStateHandle?.get(ARG_STATE) ?: initialState
        this.scope = viewModelScope
        bindInputs(inputs)
        return this
    }

    fun observe(): StateFlow<Output> = viewModelListener

    override fun process(input: I, inputStrategy: InputStrategy) {
        scope.launch {
            when (inputStrategy) {
                NONE -> inputs
                THROTTLE -> throttledInputs
                DEBOUNCE -> debouncedInputs
            }.emit(input)
        }
        return
    }

    override fun saveState(state: S) = savedStateHandle?.set(ARG_STATE, state) ?: Unit

    override fun onCleared() = Unit
}
