package com.remote_first.splash

import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import com.remote_first.shared.flow_redux.toEffectOutcome
import com.remote_first.shared.flow_redux.toResultOutcome
import com.remote_first.shared.splash.InitializeUseCase
import com.remote_first.shared.splash.NavToMain
import com.remote_first.shared.splash.RequestPermissions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SplashInputHandler @Inject constructor(private val initializeUseCase: InitializeUseCase) :
        InputHandler<SplashInput, SplashState> {
    override fun handleInputs(input: SplashInput, state: SplashState): Flow<RxOutcome> {
        return when (input) {
            is Initialize -> onInitialize()
        }
    }

    private fun onInitialize() = initializeUseCase.execute().map {
        when (it) {
            is NavToMain -> ToMain.toEffectOutcome()
            is RequestPermissions -> RequestPermissionsResult.toResultOutcome()
        }
    }
}
