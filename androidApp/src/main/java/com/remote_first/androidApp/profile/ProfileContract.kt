package com.remote_first.androidApp.profile

import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import kotlinx.android.parcel.Parcelize

sealed class ProfileInput : Input()

sealed class ProfileResult : Result

sealed class ProfileState : State

@Parcelize
object InitialState : ProfileState()

sealed class ProfileEffect : Effect
