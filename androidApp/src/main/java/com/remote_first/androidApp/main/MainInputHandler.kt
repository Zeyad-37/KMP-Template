package com.remote_first.androidApp.main

import com.remote_first.androidApp.R
import com.remote_first.androidApp.main.MainVM.Companion.SUMMARY_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.EXPLORE_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.NOTIFICATIONS_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.PROFILE_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.SPACE_X_TAB_INDEX
import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import com.remote_first.shared.flow_redux.toResultFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class MainInputHandler @Inject constructor() : InputHandler<MainInput, MainState> {

    override fun handleInputs(input: MainInput, state: MainState): Flow<RxOutcome> {
        return when (input) {
            is TabClicked -> TabClickResult(mapIdToIndex(input.tabId), input.tabId).toResultFlow()
        }
    }

    private fun mapIdToIndex(tabId: Int): Int {
        return when (tabId) {
            R.id.action_space_x -> SPACE_X_TAB_INDEX
            R.id.action_explore -> EXPLORE_TAB_INDEX
            R.id.action_summary -> SUMMARY_TAB_INDEX
            R.id.action_notifications -> NOTIFICATIONS_TAB_INDEX
            R.id.action_profile -> PROFILE_TAB_INDEX
            else -> throw IndexOutOfBoundsException("Unexpected tabId, cannot map tabId: $tabId to an index")
        }
    }
}
