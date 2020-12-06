package com.remote_first.androidApp

import android.content.Context
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completable
import com.badoo.reaktive.completable.merge
import com.badoo.reaktive.completable.observeOn
import com.badoo.reaktive.completable.subscribe
import com.badoo.reaktive.completable.subscribeOn
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.github.aakira.napier.Antilog
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.github.aakira.napier.Napier.Level.ERROR
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppConfig @Inject constructor(@ApplicationContext val context: Context) {
    fun configure() {
        asyncConfigure()
        syncConfigure()
    }

    private fun asyncConfigure() {
        lateinit var disposable: Disposable
        disposable = merge(catchingCompletable { FirebaseApp.initializeApp(context) }).observeOn(computationScheduler)
                .subscribeOn(mainScheduler).subscribe { disposable.dispose() }
    }

    private inline fun catchingCompletable(crossinline init: () -> Unit): Completable {
        return completable {
            try {
                init.invoke()
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    private fun syncConfigure() {
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        } else {
            Napier.base(object : Antilog() {
                val firebaseCrashlytics = FirebaseCrashlytics.getInstance()
                override fun performLog(
                        priority: Napier.Level, tag: String?, throwable: Throwable?, message: String?,
                ) {
                    throwable?.takeIf { priority == ERROR }?.let { firebaseCrashlytics.recordException(it) }
                }
            })
        }
    }
}