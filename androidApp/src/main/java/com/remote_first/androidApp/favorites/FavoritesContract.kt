package com.remote_first.androidApp.favorites

import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import kotlinx.android.parcel.Parcelize

sealed class FavoritesInput : Input()

sealed class FavoritesResult : Result

sealed class FavoritesState : State

@Parcelize
object InitialState : FavoritesState()

sealed class FavoritesEffect : Effect