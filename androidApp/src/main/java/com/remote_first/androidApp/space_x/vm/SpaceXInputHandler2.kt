package com.remote_first.androidApp.space_x.vm

import com.badoo.reaktive.coroutinesinterop.asObservable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.toObservable
import com.remote_first.androidApp.space_x.DoNothing
import com.remote_first.androidApp.space_x.GetLaunches
import com.remote_first.androidApp.space_x.GetLaunchesResult
import com.remote_first.androidApp.space_x.ShowEffect
import com.remote_first.androidApp.space_x.SpaceEffect
import com.remote_first.androidApp.space_x.SpaceXInput
import com.remote_first.androidApp.space_x.SpaceXState
import com.remote_first.shared.flow_redux.RxOutcome
import com.remote_first.shared.flow_redux.toEffectOutcome
import com.remote_first.shared.rx_redux.InputHandler
import com.remote_first.shared.rx_redux.executeInParallel
import com.remote_first.shared.rx_redux.mapAndCatch
import com.remote_first.shared.space_x.EmptyUseCase
import com.remote_first.shared.space_x.GetLaunchesUseCase
import com.remote_first.shared.space_x.SpaceEffectUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SpaceXInputHandler2 @Inject constructor(
        private val getLaunchesUseCase: GetLaunchesUseCase,
        private val spaceEffectUseCase: SpaceEffectUseCase,
        private val emptyUseCase: EmptyUseCase,
) : InputHandler<SpaceXInput, SpaceXState> {
    override fun handleInputs(input: SpaceXInput, state: SpaceXState): Observable<RxOutcome> {
        return when (input) {
            GetLaunches -> getLaunchesUseCase.execute().asObservable().mapAndCatch { GetLaunchesResult(mapToPO(it)) }
            ShowEffect -> spaceEffectUseCase.execute().toObservable()
                    .map { SpaceEffect.toEffectOutcome() }
                    .executeInParallel()
            DoNothing -> emptyUseCase.execute().toObservable().executeInParallel()
        }
    }
}
