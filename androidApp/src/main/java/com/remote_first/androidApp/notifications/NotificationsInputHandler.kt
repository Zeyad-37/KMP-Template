package com.remote_first.androidApp.notifications

import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationsInputHandler @Inject constructor() : InputHandler<NotificationsInput, NotificationsState> {
    override fun handleInputs(input: NotificationsInput, state: NotificationsState): Flow<RxOutcome> {
        TODO("Not yet implemented")
    }
}
