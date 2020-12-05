package com.remote_first.androidApp.space_x.vm

import com.remote_first.androidApp.space_x.FullState
import com.remote_first.androidApp.space_x.GetLaunchesResult
import com.remote_first.androidApp.space_x.InitialState
import com.remote_first.androidApp.space_x.SpaceXResult
import com.remote_first.androidApp.space_x.SpaceXState
import com.remote_first.shared.flow_redux.Reducer
import javax.inject.Inject

class SpaceXReducer @Inject constructor() : Reducer<SpaceXState, SpaceXResult> {
    override fun reduce(state: SpaceXState, result: SpaceXResult): SpaceXState {
        return when (state) {
            InitialState, is FullState -> when (result) {
                is GetLaunchesResult -> FullState(result.launches)
            }
        }
    }
}
