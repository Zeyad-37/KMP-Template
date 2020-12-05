package com.remote_first.androidApp.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContextProvider @Inject constructor(@ApplicationContext val context: Context)