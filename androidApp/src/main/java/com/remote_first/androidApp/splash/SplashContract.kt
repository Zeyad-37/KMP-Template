package com.remote_first.androidApp.splash

import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import kotlinx.android.parcel.Parcelize

sealed class SplashInput(showProgress: Boolean = true) : Input(showProgress)

object Initialize : SplashInput()

sealed class SplashState : State

@Parcelize
object InitialState : SplashState()

sealed class SplashResult : Result

sealed class SplashEffect : Effect

object ToMain : SplashEffect()
