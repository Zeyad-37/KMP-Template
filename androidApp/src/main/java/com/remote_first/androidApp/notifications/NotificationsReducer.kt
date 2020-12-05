package com.remote_first.androidApp.notifications

import com.remote_first.shared.flow_redux.Reducer
import javax.inject.Inject

class NotificationsReducer @Inject constructor() : Reducer<NotificationsState, NotificationsResult> {
    override fun reduce(state: NotificationsState, result: NotificationsResult): NotificationsState {
        TODO("Not yet implemented")
    }
}
