package com.remote_first.shared.flow_redux

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
fun <E : Effect> E.toEffectOutcome(): RxOutcome = IFlowViewModel.RxEffect(this)

@FlowPreview
@ExperimentalCoroutinesApi
fun <E : Effect> E.toEffectFlow(): Flow<RxOutcome> = toEffectOutcome().inFlow()

@FlowPreview
@ExperimentalCoroutinesApi
fun <R : Result> R.toResultOutcome(): RxOutcome = IFlowViewModel.RxResult(this)

@FlowPreview
@ExperimentalCoroutinesApi
fun <R : Result> R.toResultFlow(): Flow<RxOutcome> = toResultOutcome().inFlow()

fun Throwable.toErrorOutcome(errorMessage: String? = null): RxOutcome =
        RxError(Error(errorMessage ?: message.orEmpty(), this))

fun Flow<RxOutcome>.executeInParallel(): AsyncOutcomeFlow = AsyncOutcomeFlow(this)

fun <T> T.inFlow(): Flow<T> = flow { emit(this@inFlow) }

@FlowPreview
@ExperimentalCoroutinesApi
fun <T, R : Result> Flow<T>.mapAndCatch(errorMessage: String? = null, result: (T) -> R): Flow<RxOutcome> =
        map { result.invoke(it).toResultOutcome() }.catch { emit(it.toErrorOutcome(errorMessage)) }

@Throws(IllegalStateException::class)
inline fun <reified S : State, reified R : Result> S.throwIllegalStateException(result: R): Nothing =
        throw IllegalStateException("Can not reduce from ${S::class.simpleName} with $result")
