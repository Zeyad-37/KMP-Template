package com.remote_first.androidApp.space_x

import com.remote_first.androidApp.space_x.presentation_objects.RocketLaunchPO
import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import com.remote_first.shared.space_x.domain_objects.RocketLaunch
import kotlinx.android.parcel.Parcelize

sealed class SpaceXInput : Input()
object GetLaunches : SpaceXInput()
object ShowEffect : SpaceXInput()
object DoNothing : SpaceXInput()

sealed class SpaceXResult : Result
data class GetLaunchesResult(val launches: List<RocketLaunchPO>) : SpaceXResult()

sealed class SpaceXState : State
@Parcelize
object InitialState : SpaceXState()

@Parcelize
data class FullState(val launches: List<RocketLaunchPO>) : SpaceXState()


sealed class SpaceXEffect : Effect
object SpaceEffect : SpaceXEffect()
