package com.remote_first.androidApp.bookings

import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookingsInputHandler @Inject constructor() : InputHandler<BookingsInput, BookingsState> {
    override fun handleInputs(input: BookingsInput, state: BookingsState): Flow<RxOutcome> {
        TODO("Not yet implemented")
    }
}
