package com.remote_first.androidApp.space_x.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.remote_first.androidApp.space_x.DoNothing
import com.remote_first.androidApp.space_x.GetLaunches
import com.remote_first.androidApp.space_x.InitialState
import com.remote_first.androidApp.space_x.ShowEffect
import com.remote_first.androidApp.space_x.SpaceXEffect
import com.remote_first.androidApp.space_x.SpaceXInput
import com.remote_first.androidApp.space_x.SpaceXResult
import com.remote_first.androidApp.space_x.SpaceXState
import com.remote_first.shared.rx_redux.RxViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SpaceXVM @ViewModelInject constructor(
//        spaceXInputHandler: SpaceXInputHandler,
        spaceXInputHandler: SpaceXInputHandler2,
        spaceXReducer: SpaceXReducer,
        @Assisted savedStateHandle: SavedStateHandle?,
//) : FlowViewModel<SpaceXInput, SpaceXResult, SpaceXState, SpaceXEffect>(
) : RxViewModel<SpaceXInput, SpaceXResult, SpaceXState, SpaceXEffect>(
        spaceXInputHandler, spaceXReducer, savedStateHandle
) {
    override fun provideDefaultInitialState() = InitialState

    fun processGetLaunches() = process(GetLaunches)

    fun processShowEffect() = process(ShowEffect)

    fun processDoNothing() = process(DoNothing)
}
