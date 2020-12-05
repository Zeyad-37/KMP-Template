package com.remote_first.shared.space_x

import com.remote_first.shared.flow_redux.EmptyOutcome
import com.remote_first.shared.space_x.domain_objects.RocketLaunch
import com.remote_first.shared.space_x.domain_objects.SpaceEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class GetLaunchesUseCase @Inject constructor(private val spaceXRepo: SpaceXRepo) {
    fun execute(): Flow<List<RocketLaunch>> = spaceXRepo.getLaunches()
}

@FlowPreview
@ExperimentalCoroutinesApi
class SpaceEffectUseCase @Inject constructor() {
    fun execute() = SpaceEffect
}

class EmptyUseCase @Inject constructor() {
    fun execute() = EmptyOutcome
}
