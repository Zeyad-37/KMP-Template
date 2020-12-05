package com.remote_first.shared.rx_redux

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.onErrorReturn
import com.badoo.reaktive.observable.toObservable
import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Error
import com.remote_first.shared.flow_redux.IFlowViewModel
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.RxError
import com.remote_first.shared.flow_redux.RxOutcome
import com.remote_first.shared.flow_redux.toEffectOutcome
import com.remote_first.shared.flow_redux.toErrorOutcome
import com.remote_first.shared.flow_redux.toResultOutcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun <E : Effect> E.toEffectObservable(): Observable<RxOutcome> = toEffectOutcome().toObservable()

@FlowPreview
@ExperimentalCoroutinesApi
fun <R : Result> R.toResultObservable(): Observable<RxOutcome> = toResultOutcome().toObservable()

fun Observable<RxOutcome>.executeInParallel(): AsyncOutcomeObservable = AsyncOutcomeObservable(this)

@FlowPreview
@ExperimentalCoroutinesApi
fun <T, R : Result> Observable<T>.mapAndCatch(errorMessage: String? = null, result: (T) -> R): Observable<RxOutcome> =
        map { result.invoke(it).toResultOutcome() }.onErrorReturn { it.toErrorOutcome(errorMessage) }
