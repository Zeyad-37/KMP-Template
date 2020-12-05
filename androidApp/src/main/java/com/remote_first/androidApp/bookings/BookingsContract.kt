package com.remote_first.androidApp.bookings

import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import kotlinx.android.parcel.Parcelize

sealed class BookingsInput : Input()

sealed class BookingsResult : Result

sealed class BookingsState : State

@Parcelize
object InitialState : BookingsState()

sealed class BookingsEffect : Effect
