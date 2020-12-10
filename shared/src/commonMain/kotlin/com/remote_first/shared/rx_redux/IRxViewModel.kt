package com.remote_first.shared.rx_redux

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.ObservableObserver
import com.badoo.reaktive.observable.concatMap
import com.badoo.reaktive.observable.debounce
import com.badoo.reaktive.observable.doOnAfterNext
import com.badoo.reaktive.observable.filter
import com.badoo.reaktive.observable.flatMap
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.merge
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.ofType
import com.badoo.reaktive.observable.onErrorReturn
import com.badoo.reaktive.observable.scan
import com.badoo.reaktive.observable.share
import com.badoo.reaktive.observable.startWithValue
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.observable.threadLocal
import com.badoo.reaktive.observable.throttle
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.subject.publish.PublishSubject
import com.github.aakira.napier.Napier
import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Error
import com.remote_first.shared.flow_redux.IFlowViewModel.RxEffect
import com.remote_first.shared.flow_redux.IFlowViewModel.RxResult
import com.remote_first.shared.flow_redux.IFlowViewModel.RxState
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.InputStrategy
import com.remote_first.shared.flow_redux.LoggingListener
import com.remote_first.shared.flow_redux.LoggingListenerHelper
import com.remote_first.shared.flow_redux.Progress
import com.remote_first.shared.flow_redux.Reducer
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.RxError
import com.remote_first.shared.flow_redux.RxOutcome
import com.remote_first.shared.flow_redux.RxProgress
import com.remote_first.shared.flow_redux.State
import com.remote_first.shared.flow_redux.TrackingListener
import com.remote_first.shared.flow_redux.TrackingListenerHelper

class AsyncOutcomeObservable(val observable: Observable<RxOutcome>) : Observable<RxOutcome> {
    override fun subscribe(observer: ObservableObserver<RxOutcome>) = Unit
}

data class InputOutcomeStream(val input: Input, val outcomes: Observable<RxOutcome>)

interface IRxViewModel<I : Input, R : Result, S : State, E : Effect> {

    var disposable: Disposable
    var currentState: S
    var viewModelListener: ViewModelListener<S, E>?
    var progress: Progress

    val trackingListener: TrackingListener<I, R, S, E>
    val loggingListener: LoggingListener<I, R, S, E>
    val inputs: PublishSubject<I>
    val throttledInputs: PublishSubject<I>
    val debouncedInputs: PublishSubject<I>
    val inputHandler: InputHandler<I, S>
    val reducer: Reducer<S, R>

    /**
     * Input source provider. By default it returns empty
     * It can be overwritten to provide other inputs into the stream
     */
    fun inputSource(): Observable<I> = observable {}

    fun process(input: I, inputStrategy: InputStrategy = InputStrategy.NONE) = when (inputStrategy) {
        InputStrategy.NONE -> inputs
        InputStrategy.THROTTLE -> throttledInputs
        InputStrategy.DEBOUNCE -> debouncedInputs
    }.onNext(input)

    fun provideDefaultInitialState(): S

    fun bind(inputs: () -> Observable<I> = { observable {} }): IRxViewModel<I, R, S, E>

    fun saveState(state: S)

    fun log(): LoggingListenerHelper<I, R, S, E>.() -> Unit = {
        inputs { Napier.d("${this@IRxViewModel::class.simpleName} - Input: $it") }

        progress { Napier.d("${this@IRxViewModel::class.simpleName} - Progress: $it") }

        results { Napier.d("${this@IRxViewModel::class.simpleName} - Result: $it") }

        effects { Napier.d("${this@IRxViewModel::class.simpleName} - Effect: $it") }

        states { Napier.d("${this@IRxViewModel::class.simpleName} - State: $it") }
    }

    fun track(): TrackingListenerHelper<I, R, S, E>.() -> Unit = { /*empty*/ }

    fun bindInputs(inputs: () -> Observable<I>) {
        val outcome = createOutcomes(inputs)
        val stateResult = outcome.ofType<RxResult<R>>()
                .scan(RxState(currentState)) { state: RxState<S>, result: RxResult<R> ->
                    RxState(reducer.reduce(state.state, result.result)).apply { input = result.input }
                }.doOnAfterNext {
                    trackState(it.state, it.input as I)
                    logState(it.state)
                }
        disposable = merge(outcome.filter { it !is RxResult<*> }, stateResult)
                .doOnAfterNext {
                    trackEvents(it)
                    logEvents(it)
                }.observeOn(mainScheduler)
                .threadLocal()
                .subscribe { handleResult(it) }
    }

    private fun createOutcomes(inputs: () -> Observable<I>): Observable<RxOutcome> {
        val streamsToProcess = merge(
                inputs(), inputSource(), this.inputs, throttledInputs.throttle(InputStrategy.THROTTLE.interval),
                debouncedInputs.debounce(InputStrategy.DEBOUNCE.interval, computationScheduler)
        ).observeOn(computationScheduler)
                .doOnAfterNext {
                    trackInput(it)
                    logInput(it)
                }.map { InputOutcomeStream(it, inputHandler.handleInputs(it, currentState)) }
                .share()
        val asyncOutcomes = streamsToProcess.filter { it.outcomes is AsyncOutcomeObservable }
                .map { it.copy(outcomes = (it.outcomes as AsyncOutcomeObservable).observable) }
                .flatMap { processInputOutcomeStream(it) }
        val sequentialOutcomes = streamsToProcess.filter { it.outcomes !is AsyncOutcomeObservable }
                .concatMap { processInputOutcomeStream(it) }
        return merge(asyncOutcomes, sequentialOutcomes).share()
    }

    private fun processInputOutcomeStream(inputOutcomeStream: InputOutcomeStream): Observable<RxOutcome> {
        val result = inputOutcomeStream.outcomes
                .map { it.apply { input = inputOutcomeStream.input } }
                .onErrorReturn { createRxError(it, inputOutcomeStream.input as I) }
        return if (inputOutcomeStream.input.showProgress.not()) {
            result
        } else {
            result.startWithValue(RxProgress(Progress(isLoading = true, input = inputOutcomeStream.input)))
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

    fun notifyProgressChanged(progress: Progress) = viewModelListener?.progress?.invoke(progress)

    private fun notifyEffect(effect: E) = viewModelListener?.effects?.invoke(effect)

    private fun notifyError(error: Error) = viewModelListener?.errors?.invoke(error)

    private fun notifyNewState(state: S) {
        currentState = state
        viewModelListener?.states?.invoke(state)
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
