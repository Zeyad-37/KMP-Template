package com.remote_first.androidApp.main

import com.remote_first.shared.flow_redux.Effect
import com.remote_first.shared.flow_redux.Input
import com.remote_first.shared.flow_redux.Result
import com.remote_first.shared.flow_redux.State
import kotlinx.parcelize.Parcelize

sealed class MainInput(showProgress: Boolean) : Input(showProgress)
data class TabClicked(val tabId: Int): MainInput(false)

@Parcelize
data class MainState(
        val index: Int,
        val title: String
) : State

sealed class MainResult : Result
data class TabClickResult(val index: Int, val title: String) : MainResult()

sealed class MainEffect : Effect
