package com.remote_first.androidApp.notifications

import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import kotlinx.android.parcel.Parcelize

sealed class NotificationsInput : Input()

sealed class NotificationsResult : Result

sealed class NotificationsState : State

@Parcelize
object InitialState : NotificationsState()

sealed class NotificationsEffect : Effect
