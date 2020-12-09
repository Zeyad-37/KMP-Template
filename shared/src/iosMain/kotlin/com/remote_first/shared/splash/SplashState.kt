package com.remote_first.shared.splash

import com.remote_first.shared.flow_redux.State

actual sealed class SplashState : State
actual object InitialState : SplashState()
actual object RequestPermissions