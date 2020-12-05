package com.remote_first.androidApp.di

import android.content.Context
import com.remote_first.shared.cache.AppDatabase
import com.remote_first.shared.db.Database
import com.remote_first.shared.db.DatabaseDriverFactory
import com.remote_first.shared.network.HttpNetworkTransport
import com.remote_first.shared.network.NetworkTransport
import com.remote_first.shared.space_x.SpaceXApi
import com.squareup.sqldelight.db.SqlDriver
import comremotefirstsharedcache.AppDatabaseQueries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideDatabaseDriverFactory(@ApplicationContext appContext: Context) = DatabaseDriverFactory(appContext)

    @Provides
    fun provideSqlDriver(databaseDriverFactory: DatabaseDriverFactory): SqlDriver = databaseDriverFactory.createDriver()

    @Provides
    fun provideAppDatabase(databaseDriverFactory: DatabaseDriverFactory): AppDatabase =
            AppDatabase(databaseDriverFactory.createDriver())

    @Provides
    fun provideAppDatabaseQueries(appDatabase: AppDatabase): AppDatabaseQueries = appDatabase.appDatabaseQueries

    @Provides
    fun provideDatabase(appDatabaseQueries: AppDatabaseQueries): Database = Database(appDatabaseQueries)

    @Provides
    fun provideHttpClient(networkTransport: NetworkTransport) = networkTransport.httpClient()

    @Provides
    fun provideNetworkTransport(): NetworkTransport = HttpNetworkTransport

    @Provides
    fun provideSpaceXApi(httpClient: HttpClient) = SpaceXApi(httpClient)
}
