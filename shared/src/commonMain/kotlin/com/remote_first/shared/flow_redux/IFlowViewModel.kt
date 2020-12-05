package com.remote_first.shared.flow_redux

import com.github.aakira.napier.Napier
import com.remote_first.shared.flow_redux.InputStrategy.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

const val ARG_STATE = "arg_state"

internal object EmptyInput : Input()

open class RxOutcome(open var input: Input = EmptyInput)

object EmptyOutcome : RxOutcome()

data class RxProgress(val progress: Progress) : RxOutcome()
internal data class RxError(var error: Error) : RxOutcome() {
    override var input: Input = EmptyInput
        set(value) {
            error = error.copy(input = value)
            field = value
        }
}

class AsyncOutcomeFlow(val observable: Flow<RxOutcome>) : Flow<RxOutcome> {
    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<RxOutcome>) = Unit
}

data class InputOutcomeFlow(val input: Input, val outcomes: Flow<RxOutcome>)

@FlowPreview
@ExperimentalCoroutinesApi
interface IFlowViewModel<I : Input, R : Result, S : State, E : Effect> {

    data class RxState<S>(val state: S) : RxOutcome()
    data class RxEffect<E>(val effect: E) : RxOutcome()
    data class RxResult<R>(val result: R) : RxOutcome()

    var currentState: S
    var viewModelListener: MutableStateFlow<Output>
    var trackingListener: TrackingListener<I, R, S, E>
    var loggingListener: LoggingListener<I, R, S, E>
    var progress: Progress
    var scope: CoroutineScope

    val inputs: MutableSharedFlow<I>
    val throttledInputs: MutableSharedFlow<I>
    val debouncedInputs: MutableSharedFlow<I>
    val inputHandler: InputHandler<I, S>
    val reducer: Reducer<S, R>

    /**
     * Input source provider. By default it returns empty
     * It can be overwritten to provide other inputs into the stream
     */
    fun inputSource(): MutableSharedFlow<I> = MutableSharedFlow()

    fun process(input: I, inputStrategy: InputStrategy = NONE)

    fun provideDefaultInitialState(): S

    fun bind(inputs: () -> MutableSharedFlow<I> = { MutableSharedFlow() }): IFlowViewModel<I, R, S, E>

    fun saveState(state: S)

    fun log(): LoggingListenerHelper<I, R, S, E>.() -> Unit = {
        inputs { Napier.d("${this@IFlowViewModel::class.simpleName} - Input: $it") }

        progress { Napier.d("${this@IFlowViewModel::class.simpleName} - Progress: $it") }

        results { Napier.d("${this@IFlowViewModel::class.simpleName} - Result: $it") }

        effects { Napier.d("${this@IFlowViewModel::class.simpleName} - Effect: $it") }

        states { Napier.d("${this@IFlowViewModel::class.simpleName} - State: $it") }
    }

    fun track(): TrackingListenerHelper<I, R, S, E>.() -> Unit = { /*empty*/ }

    fun bindInputs(inputs: () -> MutableSharedFlow<I>) {
        scope.launch(Dispatchers.Main) {
            val outcome = createOutcomes(inputs)
            val stateResult = outcome.filter { it is RxResult<*> }.map { it as RxResult<R> }
                    .scan(RxState(currentState)) { state: RxState<S>, result: RxResult<R> ->
                        RxState(reducer.reduce(state.state, result.result)).apply { input = result.input }
                    }.onEach {
                        trackState(it.state, it.input as I)
                        logState(it.state)
                    }
            merge(outcome.filter { it !is RxResult<*> }, stateResult)
                    .onEach {
                        trackEvents(it)
                        logEvents(it)
                    }.flowOn(Dispatchers.Default)
                    .collect { handleResult(it) }
        }
    }

    private fun createOutcomes(inputs: () -> MutableSharedFlow<I>): Flow<RxOutcome> {
        val streamsToProcess =
                merge(
                        inputs(), inputSource(), this.inputs,
                        debouncedInputs.debounce(DEBOUNCE.interval),
                        throttledInputs.onEach { delay(THROTTLE.interval) },
                ).onEach {
                    trackInput(it)
                    logInput(it)
                }.map { InputOutcomeFlow(it, inputHandler.handleInputs(it, currentState)) }

        val asyncOutcomes = streamsToProcess.filter { it.outcomes is AsyncOutcomeFlow }
                .map { it.copy(outcomes = (it.outcomes as AsyncOutcomeFlow).observable) }
                .flatMapMerge { processInputOutcomeStream(it) }
        val sequentialOutcomes = streamsToProcess.filter { it.outcomes !is AsyncOutcomeFlow }
                .flatMapConcat { processInputOutcomeStream(it) }
        return merge(asyncOutcomes, sequentialOutcomes)
    }

    private fun processInputOutcomeStream(inputOutcomeStream: InputOutcomeFlow): Flow<RxOutcome> {
        val result = inputOutcomeStream.outcomes
                .map { it.apply { input = inputOutcomeStream.input } }
                .catch { emit(createRxError(it, inputOutcomeStream.input as I)) }
        return if (inputOutcomeStream.input.showProgress.not()) {
            result
        } else {
            result.onStart { emit(RxProgress(Progress(isLoading = true, input = inputOutcomeStream.input))) }
        }
    }

    private fun trackEvents(event: RxOutcome) {
        when (event) {
            is RxProgress -> trackingListener.progress(event.progress)
            is RxEffect<*> -> trackingListener.effects(event.effect as E, event.input as I)
            is RxError -> trackingListener.errors(event.error)
            is RxResult<*> -> trackingListener.results(event.result as R, event.input as I)
        }
    }

    private fun logEvents(event: RxOutcome) {
        when (event) {
            is RxProgress -> loggingListener.progress(event.progress)
            is RxEffect<*> -> loggingListener.effects(event.effect as E)
            is RxError -> loggingListener.errors(event.error)
            is RxResult<*> -> loggingListener.results(event.result as R)
        }
    }

    private fun trackInput(input: I) = trackingListener.inputs(input)

    private fun logInput(input: I) = loggingListener.inputs(input)

    private fun trackState(state: S, input: I) = trackingListener.states(state, input)

    private fun logState(state: S) = loggingListener.states(state)

    private fun createRxError(throwable: Throwable, input: I): RxError =
            RxError(Error(throwable.message.orEmpty(), throwable, input)).apply { this.input = input }

    private fun handleResult(result: RxOutcome) {
        if (result is RxProgress) {
            notifyProgressChanged(result.progress)
        } else {
            dismissProgressDependingOnInput(result.input as I)
        }
        when (result) {
            is RxError -> notifyError(result.error)
            is RxEffect<*> -> notifyEffect(result.effect as E)
            is RxState<*> -> {
                saveState(result.state as S)
                notifyNewState(result.state)
            }
        }
    }

    private fun dismissProgressDependingOnInput(input: I?) {
        if (input?.showProgress == true) {
            notifyProgressChanged(Progress(false, input))
        }
    }

    fun notifyProgressChanged(progress: Progress) {
        scope.launch { viewModelListener.emit(progress) }
    }

    private fun notifyEffect(effect: E) = scope.launch { viewModelListener.emit(effect) }

    private fun notifyError(error: Error) = scope.launch { viewModelListener.emit(error) }

    private fun notifyNewState(state: S) {
        currentState = state
        scope.launch { viewModelListener.emit(state) }
    }

    fun initTracking(): TrackingListener<I, R, S, E> {
        val trackingListenerHelper = TrackingListenerHelper<I, R, S, E>()
        val init: TrackingListenerHelper<I, R, S, E>.() -> Unit = track()
        trackingListenerHelper.init()
        return trackingListenerHelper
    }

    fun initLogging(): LoggingListener<I, R, S, E> {
        val loggingListenerHelper = LoggingListenerHelper<I, R, S, E>()
        val init: LoggingListenerHelper<I, R, S, E>.() -> Unit = log()
        loggingListenerHelper.init()
        return loggingListenerHelper
    }
}
