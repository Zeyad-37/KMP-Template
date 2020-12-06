package com.remote_first.androidApp.main

import android.app.Activity
import com.remote_first.navigation.IMainActivityNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

class MainActivityNavigator @Inject constructor() : IMainActivityNavigator {
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun navigate(fromActivity: Activity) {
        fromActivity.startActivity(MainActivity.makeIntent(fromActivity))
    }
}
