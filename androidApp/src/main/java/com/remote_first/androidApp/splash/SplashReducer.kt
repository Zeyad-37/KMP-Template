package com.remote_first.androidApp.splash

import com.remote_first.shared.flow_redux.Reducer
import javax.inject.Inject

class SplashReducer @Inject constructor() : Reducer<SplashState, SplashResult> {
    override fun reduce(state: SplashState, result: SplashResult): SplashState {
        TODO("Not yet implemented")
    }
}
