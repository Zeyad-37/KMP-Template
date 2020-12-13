package com.remote_first.androidApp.explore

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.remote_first.shared.flow_redux.FlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class ExploreVM @ViewModelInject constructor(
        exploreInputHandler: ExploreInputHandler,
        exploreReducer: ExploreReducer,
        @Assisted savedStateHandle: SavedStateHandle?,
) : FlowViewModel<ExploreInput, ExploreResult, ExploreState, ExploreEffect>(
        exploreInputHandler, exploreReducer, savedStateHandle
) {
    fun provideInitialState() = InitialState
}
