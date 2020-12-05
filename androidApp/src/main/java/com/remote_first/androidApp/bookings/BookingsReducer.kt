package com.remote_first.androidApp.bookings

import com.remote_first.shared.flow_redux.Reducer
import javax.inject.Inject

class BookingsReducer @Inject constructor() : Reducer<BookingsState, BookingsResult> {
    override fun reduce(state: BookingsState, result: BookingsResult): BookingsState {
        TODO("Not yet implemented")
    }
}
