package com.remote_first.androidApp.space_x.vm

import com.remote_first.androidApp.space_x.DoNothing
import com.remote_first.androidApp.space_x.GetLaunches
import com.remote_first.androidApp.space_x.GetLaunchesResult
import com.remote_first.androidApp.space_x.ShowEffect
import com.remote_first.androidApp.space_x.SpaceEffect
import com.remote_first.androidApp.space_x.SpaceXInput
import com.remote_first.androidApp.space_x.SpaceXState
import com.remote_first.androidApp.space_x.presentation_objects.LinksPO
import com.remote_first.androidApp.space_x.presentation_objects.RocketLaunchPO
import com.remote_first.androidApp.space_x.presentation_objects.RocketPO
import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import com.remote_first.shared.flow_redux.executeInParallel
import com.remote_first.shared.flow_redux.inFlow
import com.remote_first.shared.flow_redux.mapAndCatch
import com.remote_first.shared.flow_redux.toEffectOutcome
import com.remote_first.shared.space_x.EmptyUseCase
import com.remote_first.shared.space_x.GetLaunchesUseCase
import com.remote_first.shared.space_x.SpaceEffectUseCase
import com.remote_first.shared.space_x.domain_objects.RocketLaunch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SpaceXInputHandler @Inject constructor(
        private val getLaunchesUseCase: GetLaunchesUseCase,
        private val spaceEffectUseCase: SpaceEffectUseCase,
        private val emptyUseCase: EmptyUseCase,
) : InputHandler<SpaceXInput, SpaceXState> {
    override fun handleInputs(input: SpaceXInput, state: SpaceXState): Flow<RxOutcome> {
        return when (input) {
            GetLaunches -> getLaunchesUseCase.execute().mapAndCatch { GetLaunchesResult(mapToPO(it)) }
            ShowEffect -> spaceEffectUseCase.execute().inFlow()
                    .map { SpaceEffect.toEffectOutcome() }
                    .executeInParallel()
            DoNothing -> emptyUseCase.execute().inFlow().executeInParallel()
        }
    }
}

fun mapToPO(domainList: List<RocketLaunch>): List<RocketLaunchPO> {
    return domainList.map {
        RocketLaunchPO(it.flightNumber,
                it.missionName,
                it.launchYear,
                it.launchDateUTC,
                RocketPO(it.rocket.id, it.rocket.name, it.rocket.type),
                it.details,
                it.launchSuccess,
                LinksPO(it.links.missionPatchUrl, it.links.articleUrl))
    }
}
