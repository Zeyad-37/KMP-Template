package com.remote_first.androidApp.profile

import com.remote_first.shared.flow_redux.Reducer
import javax.inject.Inject

class ProfileReducer @Inject constructor() : Reducer<ProfileState, ProfileResult> {
    override fun reduce(state: ProfileState, result: ProfileResult): ProfileState {
        TODO("Not yet implemented")
    }
}
