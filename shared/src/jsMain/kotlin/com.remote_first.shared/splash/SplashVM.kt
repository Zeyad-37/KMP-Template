package com.remote_first.shared.splash

import com.remote_first.shared.flow_redux.FlowViewModel

actual class SplashVM(splashInputHandler: SplashInputHandler, reducer: SplashReducer) :
        FlowViewModel<SplashInput, SplashResult, SplashState, SplashEffect>(splashInputHandler, reducer) {
    override fun provideDefaultInitialState() = InitialState
}
