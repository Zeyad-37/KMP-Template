package com.remote_first.splash

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import com.remote_first.shared.flow_redux.toEffectOutcome
import com.remote_first.shared.flow_redux.toResultOutcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SplashInputHandler @Inject constructor(private val permissionChecker: PermissionChecker) :
        InputHandler<SplashInput, SplashState> {
    override fun handleInputs(input: SplashInput, state: SplashState): Flow<RxOutcome> {
        return when (input) {
            is Initialize -> onInitialize()
        }
    }

    private fun onInitialize(): Flow<RxOutcome> {
        return flow {
            if (permissionChecker.hasPermission(ACCESS_FINE_LOCATION) &&
                    permissionChecker.hasPermission(ACCESS_COARSE_LOCATION)) {
                delay(1000)
                emit(ToMain.toEffectOutcome())
            } else {
                emit(RequestPermissionsResult.toResultOutcome())
            }
        }
    }
}
