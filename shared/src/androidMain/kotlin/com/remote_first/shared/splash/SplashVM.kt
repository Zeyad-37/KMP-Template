package com.remote_first.shared.splash

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.remote_first.shared.flow_redux.FlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
actual class SplashVM @ViewModelInject constructor(
        splashInputHandler: SplashInputHandler, reducer: SplashReducer, @Assisted savedStateHandle: SavedStateHandle?,
) : FlowViewModel<SplashInput, SplashResult, SplashState, SplashEffect>(
        splashInputHandler, reducer, savedStateHandle
) {
    fun provideInitialState() = InitialState

    fun processInitialize() = process(Initialize)
}
