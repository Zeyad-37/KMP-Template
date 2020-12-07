package com.remote_first.navigation

import android.content.Context
import android.content.Intent

interface IMainActivityIntentFactory {
    fun create(context: Context): Intent
}
