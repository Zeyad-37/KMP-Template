package com.remote_first.androidApp.favorites

import com.remote_first.shared.flow_redux.Reducer
import javax.inject.Inject

class FavoritesReducer @Inject constructor() : Reducer<FavoritesState, FavoritesResult> {
    override fun reduce(state: FavoritesState, result: FavoritesResult): FavoritesState {
        TODO("Not yet implemented")
    }
}
