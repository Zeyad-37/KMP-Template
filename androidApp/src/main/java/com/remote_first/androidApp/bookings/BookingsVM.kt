package com.remote_first.androidApp.bookings

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.remote_first.shared.flow_redux.FlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class BookingsVM @ViewModelInject constructor(
        bookingsInputHandler: BookingsInputHandler,
        bookingsReducer: BookingsReducer,
        @Assisted savedStateHandle: SavedStateHandle?,
) : FlowViewModel<BookingsInput, BookingsResult, BookingsState, BookingsEffect>(
        bookingsInputHandler, bookingsReducer, savedStateHandle
) {
    fun provideInitialState() = InitialState
}
