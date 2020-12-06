package com.remote_first.shared.splash

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
actual class InitializeUseCase @Inject constructor(@ApplicationContext val context: Context) {
    actual fun execute(): Flow<SplashOutput> {
        return flow {
            if (checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
                    && checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {
                emit(NavToMain)
            } else {
                emit(RequestPermissions)
            }
        }
    }
}
