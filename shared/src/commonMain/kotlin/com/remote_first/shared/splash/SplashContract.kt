package com.remote_first.shared.splash

import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State

sealed class SplashInput(showProgress: Boolean = true) : Input(showProgress)

object Initialize : SplashInput()

expect sealed class SplashState : State

expect object InitialState : SplashState

expect object RequestPermissions : SplashState

sealed class SplashResult : Result

object RequestPermissionsResult : SplashResult()

sealed class SplashEffect : Effect

object ToMain : SplashEffect()
