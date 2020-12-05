package com.remote_first.androidApp.splash

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionChecker @Inject constructor(@ApplicationContext private val context: Context) {

    fun hasPermission(permission: String): Boolean =
            ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED
}
