package com.remote_first.shared.space_x

import com.remote_first.shared.flow_redux.EmptyOutcome
import com.remote_first.shared.space_x.domain_objects.RocketLaunch
import com.remote_first.shared.space_x.domain_objects.SpaceEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ExperimentalCoroutinesApi
class GetLaunchesUseCase(private val spaceXRepo: SpaceXRepo) {
    fun execute(): Flow<List<RocketLaunch>> = spaceXRepo.getLaunches()
}

class SpaceEffectUseCase {
    fun execute() = SpaceEffect
}

class EmptyUseCase {
    fun execute() = EmptyOutcome
}
