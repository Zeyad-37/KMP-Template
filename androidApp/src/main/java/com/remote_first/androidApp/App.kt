package com.remote_first.androidApp

import android.app.Application
import com.github.aakira.napier.Antilog
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.github.aakira.napier.Napier.Level.ERROR
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        } else {
            Napier.base(object : Antilog() {
                val firebaseCrashlytics = FirebaseCrashlytics.getInstance()
                override fun performLog(priority: Napier.Level, tag: String?, throwable: Throwable?, message: String?) {
                    throwable?.takeIf { priority == ERROR }?.let { firebaseCrashlytics.recordException(it) }
                }
            })
        }
    }
}
