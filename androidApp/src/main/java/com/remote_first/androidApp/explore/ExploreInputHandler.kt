package com.remote_first.androidApp.explore

import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExploreInputHandler @Inject constructor() : InputHandler<ExploreInput, ExploreState> {
    override fun handleInputs(input: ExploreInput, state: ExploreState): Flow<RxOutcome> {
        TODO("Not yet implemented")
    }
}
