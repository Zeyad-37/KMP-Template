package com.remote_first.androidApp.explore

import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import kotlinx.android.parcel.Parcelize

sealed class ExploreInput : Input()

sealed class ExploreResult : Result

sealed class ExploreState : State

@Parcelize
object InitialState : ExploreState()

sealed class ExploreEffect : Effect
