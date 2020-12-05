package com.remote_first.androidApp.notifications

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.remote_first.shared.flow_redux.FlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class NotificationVM @ViewModelInject constructor(
        notificationsInputHandler: NotificationsInputHandler,
        notificationsReducer: NotificationsReducer,
        @Assisted savedStateHandle: SavedStateHandle?,
) : FlowViewModel<NotificationsInput, NotificationsResult, NotificationsState, NotificationsEffect>(
        notificationsInputHandler, notificationsReducer, savedStateHandle
) {
    override fun provideDefaultInitialState() = InitialState
}
