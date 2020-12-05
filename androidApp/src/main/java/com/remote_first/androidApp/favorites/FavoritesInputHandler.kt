package com.remote_first.androidApp.favorites

import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesInputHandler @Inject constructor() : InputHandler<FavoritesInput, FavoritesState> {
    override fun handleInputs(input: FavoritesInput, state: FavoritesState): Flow<RxOutcome> {
        TODO("Not yet implemented")
    }
}
