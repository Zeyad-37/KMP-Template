package com.remote_first.shared.rx_redux

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.scheduler.Scheduler
import com.badoo.reaktive.subject.publish.PublishSubject
import com.remote_first.shared.flow_redux.ARG_STATE
import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.EmptyInput
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.LoggingListener
import com.remote_first.shared.flow_redux.Progress
import com.remote_first.shared.flow_redux.Reducer
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import com.remote_first.shared.flow_redux.TrackingListener
import kotlin.properties.Delegates
import kotlin.properties.Delegates.observable

actual abstract class RxViewModel<I : Input, R : Result, S : State, E : Effect>(
        override val inputHandler: InputHandler<I, S>,
        override val reducer: Reducer<S, R>,
        private val savedStateHandle: SavedStateHandle?,
        override val computationScheduler: Scheduler = com.badoo.reaktive.scheduler.computationScheduler,
        override val mainScheduler: Scheduler = com.badoo.reaktive.scheduler.mainScheduler,
) : IRxViewModel<I, R, S, E>, ViewModel() {

    override lateinit var disposable: Disposable
    override lateinit var currentState: S
    override var viewModelListener: ViewModelListener<S, E>? = null
        set(value) {
            value?.states?.invoke(currentState)
            field = value
        }
    override var progress: Progress by observable(Progress(false, EmptyInput),
            { _, oldValue, newValue ->
                if (newValue != oldValue) notifyProgressChanged(newValue)
            })

    override val inputs: PublishSubject<I> = PublishSubject()
    override val throttledInputs: PublishSubject<I> = PublishSubject()
    override val debouncedInputs: PublishSubject<I> = PublishSubject()
    override val trackingListener: TrackingListener<I, R, S, E> = this.initTracking()
    override val loggingListener: LoggingListener<I, R, S, E> = this.initLogging()

    override fun bind(initialState: S, inputs: () -> Observable<I>): RxViewModel<I, R, S, E> {
        currentState = savedStateHandle?.get(ARG_STATE) ?: initialState
        bindInputs(inputs)
        return this
    }

    override fun saveState(state: S) = savedStateHandle?.set(ARG_STATE, state) ?: Unit

    fun observe(lifecycle: Lifecycle, init: ViewModelListenerHelper<S, E>.() -> Unit) {
        val helper = ViewModelListenerHelper<S, E>()
        helper.init()
        viewModelListener = helper
        removeObservers(lifecycle)
    }

    private fun removeObservers(lifecycle: Lifecycle) =
            lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    unBind()
                    lifecycle.removeObserver(this)
                }
            })

    override fun onCleared() = unBind()

    private fun unBind() {
        viewModelListener = null
        disposable.dispose()
    }
}
