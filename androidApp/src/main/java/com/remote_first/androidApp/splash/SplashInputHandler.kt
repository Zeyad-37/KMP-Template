package com.remote_first.androidApp.splash

import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import com.remote_first.shared.flow_redux.toEffectFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SplashInputHandler @Inject constructor() : InputHandler<SplashInput, SplashState> {
    override fun handleInputs(input: SplashInput, state: SplashState): Flow<RxOutcome> {
        return when (input) {
            is Initialize -> ToMain.toEffectFlow().onEach { delay(1000) }
        }
    }
}
