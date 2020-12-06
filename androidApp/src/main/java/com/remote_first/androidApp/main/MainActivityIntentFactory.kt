package com.remote_first.androidApp.main

import android.content.Context
import com.remote_first.navigation.IMainActivityIntentFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

class MainActivityIntentFactory @Inject constructor() : IMainActivityIntentFactory {
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun create(context: Context) = MainActivity.makeIntent(context)
}
