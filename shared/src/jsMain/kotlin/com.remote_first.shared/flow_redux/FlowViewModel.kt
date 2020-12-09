package com.remote_first.shared.flow_redux

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@FlowPreview
@ExperimentalCoroutinesApi
actual abstract class FlowViewModel<I : Input, R : Result, S : State, E : Effect>(
        override val inputHandler: InputHandler<I, S>,
        override val reducer: Reducer<S, R>,
) : IFlowViewModel<I, R, S, E> {
    override lateinit var currentState: S
    override lateinit var scope: CoroutineScope
    override lateinit var viewModelListener: MutableStateFlow<Output>
    override var progress: Progress by Delegates.observable(Progress(false, EmptyInput),
            { _, oldValue, newValue ->
                if (newValue != oldValue) notifyProgressChanged(newValue)
            })
    override var trackingListener: TrackingListener<I, R, S, E> = this.initTracking()
    override var loggingListener: LoggingListener<I, R, S, E> = this.initLogging()

    override val inputs: MutableSharedFlow<I> = MutableSharedFlow()
    override val throttledInputs: MutableSharedFlow<I> = MutableSharedFlow()
    override val debouncedInputs: MutableSharedFlow<I> = MutableSharedFlow()

//    private var savedStateHandle: SavedStateHandle? = null TODO

    override fun bind(inputs: () -> MutableSharedFlow<I>): FlowViewModel<I, R, S, E> {
        currentState = /*savedStateHandle?.get(ARG_STATE) ?:*/ provideDefaultInitialState()
        scope = TODO()
        bindInputs(inputs)
        return this
    }

    fun observe(): StateFlow<Output> {
        viewModelListener = MutableStateFlow(currentState as Output)
        return viewModelListener
    }

    override fun process(input: I, inputStrategy: InputStrategy) {
        scope.launch {
            when (inputStrategy) {
                InputStrategy.NONE -> inputs
                InputStrategy.THROTTLE -> throttledInputs
                InputStrategy.DEBOUNCE -> debouncedInputs
            }.emit(input)
        }
        return
    }

    override fun saveState(state: S) = /*savedStateHandle?.set(ARG_STATE, state) ?:*/ Unit
}