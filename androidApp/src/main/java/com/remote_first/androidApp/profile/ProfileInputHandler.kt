package com.remote_first.androidApp.profile

import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileInputHandler @Inject constructor() : InputHandler<ProfileInput, ProfileState> {
    override fun handleInputs(input: ProfileInput, state: ProfileState): Flow<RxOutcome> {
        TODO("Not yet implemented")
    }
}
