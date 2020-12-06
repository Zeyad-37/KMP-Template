package com.remote_first.shared.splash

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ExperimentalCoroutinesApi
expect class InitializeUseCase {
    fun execute(): Flow<SplashOutput>
}
