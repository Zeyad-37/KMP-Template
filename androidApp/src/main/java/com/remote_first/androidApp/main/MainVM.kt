package com.remote_first.androidApp.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.remote_first.androidApp.R
import com.remote_first.androidApp.utils.ContextProvider
import com.remote_first.shared.flow_redux.FlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class MainVM @ViewModelInject constructor(
        mainInputHandler: MainInputHandler, reducer: MainReducer, @Assisted savedStateHandle: SavedStateHandle?,
        private val contextProvider: ContextProvider,
) : FlowViewModel<MainInput, MainResult, MainState, MainEffect>(
        mainInputHandler, reducer, savedStateHandle
) {

    companion object {
        const val SPACE_X_TAB_INDEX = 0
        const val EXPLORE_TAB_INDEX = 1
        const val SUMMARY_TAB_INDEX = 2
        const val NOTIFICATIONS_TAB_INDEX = 3
        const val PROFILE_TAB_INDEX = 4
        const val BOTTOM_NAVIGATION_SIZE = 5
    }

    fun processTabClicked(tabId: Int) = process(TabClicked(tabId))

    override fun provideDefaultInitialState() = MainState(SPACE_X_TAB_INDEX,
            R.id.action_space_x, contextProvider.context.getString(R.string.space_x_tab_title))
}
