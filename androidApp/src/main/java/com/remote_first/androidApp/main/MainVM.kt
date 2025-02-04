package com.remote_first.androidApp.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.remote_first.androidApp.R
import com.remote_first.androidApp.utils.ContextProvider
import com.remote_first.shared.event_bus.EventBusService
import com.remote_first.shared.flow_redux.FlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
class MainVM @ViewModelInject constructor(
        mainInputHandler: MainInputHandler, reducer: MainReducer, @Assisted savedStateHandle: SavedStateHandle?,
        private val contextProvider: ContextProvider, private val eventBusService: EventBusService,
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

    fun provideInitialState() = MainState(SPACE_X_TAB_INDEX,
            contextProvider.context.getString(R.string.space_x_tab_title))

    override fun inputSource(): Flow<MainInput> =
            eventBusService.observe().map { TabClicked(R.id.action_profile) }
}
