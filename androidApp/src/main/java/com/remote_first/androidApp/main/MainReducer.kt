package com.remote_first.androidApp.main

import com.remote_first.shared.flow_redux.Reducer
import javax.inject.Inject

class MainReducer @Inject constructor(): Reducer<MainState, MainResult> {
    override fun reduce(state: MainState, result: MainResult): MainState {
        return when (result) {
            is TabClickResult -> with(result) { state.copy(index = index, tabId = tabId, title = title) }
        }
    }
}
