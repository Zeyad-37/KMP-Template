package com.remote_first.shared.splash

import com.remote_first.shared.flow_redux.State
import kotlinx.parcelize.Parcelize

actual sealed class SplashState : State

@Parcelize
actual object InitialState : SplashState()

@Parcelize
actual object RequestPermissions : SplashState()
