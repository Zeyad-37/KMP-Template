package com.remote_first.androidApp.favorites

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.remote_first.shared.flow_redux.FlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class FavoritesVM @ViewModelInject constructor(
        bookingsInputHandler: FavoritesInputHandler,
        bookingsReducer: FavoritesReducer,
        @Assisted savedStateHandle: SavedStateHandle?,
) : FlowViewModel<FavoritesInput, FavoritesResult, FavoritesState, FavoritesEffect>(
        bookingsInputHandler, bookingsReducer, savedStateHandle
) {
    override fun provideDefaultInitialState() = InitialState
}
