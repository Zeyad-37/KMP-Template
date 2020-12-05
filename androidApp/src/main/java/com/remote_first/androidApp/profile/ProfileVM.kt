package com.remote_first.androidApp.profile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.remote_first.shared.flow_redux.FlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class ProfileVM @ViewModelInject constructor(
        profileInputHandler: ProfileInputHandler,
        profileReducer: ProfileReducer,
        @Assisted savedStateHandle: SavedStateHandle?,
) : FlowViewModel<ProfileInput, ProfileResult, ProfileState, ProfileEffect>(
        profileInputHandler, profileReducer, savedStateHandle
) {
    override fun provideDefaultInitialState() = InitialState
}
