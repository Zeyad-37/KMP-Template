package com.remote_first.androidApp.splash

import com.remote_first.shared.flow_redux.Reducer
import com.remote_first.shared.flow_redux.throwIllegalStateException
import javax.inject.Inject

class SplashReducer @Inject constructor() : Reducer<SplashState, SplashResult> {
    override fun reduce(state: SplashState, result: SplashResult): SplashState {
        return when(state) {
            is InitialState -> when (result) {
                is RequestPermissionsResult -> RequestPermissions
            }
            is RequestPermissions -> when (result) {
                is RequestPermissionsResult -> state.throwIllegalStateException(result)
            }
        }
    }
}
