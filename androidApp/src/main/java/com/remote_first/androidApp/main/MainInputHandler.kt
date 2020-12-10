package com.remote_first.androidApp.main

import com.remote_first.androidApp.R
import com.remote_first.androidApp.main.MainVM.Companion.EXPLORE_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.NOTIFICATIONS_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.PROFILE_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.SPACE_X_TAB_INDEX
import com.remote_first.androidApp.main.MainVM.Companion.SUMMARY_TAB_INDEX
import com.remote_first.androidApp.utils.ContextProvider
import com.remote_first.shared.flow_redux.InputHandler
import com.remote_first.shared.flow_redux.RxOutcome
import com.remote_first.shared.flow_redux.toResultFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class MainInputHandler @Inject constructor(private val contextProvider: ContextProvider) :
        InputHandler<MainInput, MainState> {

    override fun handleInputs(input: MainInput, state: MainState): Flow<RxOutcome> {
        return when (input) {
            is TabClicked -> {
                val title = mapIndexToTitle(input.tabIndex)
                TabClickResult(input.tabIndex, title).toResultFlow()
            }
        }
    }

    private fun mapIndexToTitle(tabIndex: Int): String {
        return when (tabIndex) {
            SPACE_X_TAB_INDEX -> contextProvider.context.getString(R.string.space_x_tab_title)
            EXPLORE_TAB_INDEX -> contextProvider.context.getString(R.string.explore_tab_title)
            SUMMARY_TAB_INDEX -> contextProvider.context.getString(R.string.summary)
            NOTIFICATIONS_TAB_INDEX -> contextProvider.context.getString(R.string.notifications)
            PROFILE_TAB_INDEX -> contextProvider.context.getString(R.string.profile)
            else -> throw IndexOutOfBoundsException("Unexpected tabIndex, cannot map tabIndex: $tabIndex to a title")
        }
    }
}
