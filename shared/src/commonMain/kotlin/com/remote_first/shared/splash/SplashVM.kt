package com.remote_first.shared.splash

import com.remote_first.shared.flow_redux.FlowViewModel

expect class SplashVM : FlowViewModel<SplashInput, SplashResult, SplashState, SplashEffect>
