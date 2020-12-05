package com.remote_first.shared.db

import android.content.Context
import com.remote_first.shared.cache.AppDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import javax.inject.Inject

actual class DatabaseDriverFactory @Inject constructor(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "test.db")
    }
}
