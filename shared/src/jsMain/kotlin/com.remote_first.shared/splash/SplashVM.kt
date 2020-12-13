package com.remote_first.shared.splash

import com.remote_first.shared.flow_redux.FlowViewModel
import kotlinx.coroutines.CoroutineDispatcher

actual class SplashVM(
        splashInputHandler: SplashInputHandler, reducer: SplashReducer,
        override val computationDispatcher: CoroutineDispatcher,
        override val mainDispatcher: CoroutineDispatcher,
) : FlowViewModel<SplashInput, SplashResult, SplashState, SplashEffect>(splashInputHandler, reducer) {
    fun provideInitialState() = InitialState
}
