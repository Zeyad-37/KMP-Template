package com.remote_first.androidApp.di

import android.content.Context
import com.remote_first.androidApp.main.MainActivityIntentFactory
import com.remote_first.androidApp.utils.ContextProvider
import com.remote_first.navigation.IMainActivityIntentFactory
import com.remote_first.shared.cache.AppDatabase
import com.remote_first.shared.db.Database
import com.remote_first.shared.db.DatabaseDriverFactory
import com.remote_first.shared.event_bus.EventBus
import com.remote_first.shared.event_bus.EventBusService
import com.remote_first.shared.network.HttpNetworkTransport
import com.remote_first.shared.network.NetworkTransport
import com.remote_first.shared.space_x.EmptyUseCase
import com.remote_first.shared.space_x.GetLaunchesUseCase
import com.remote_first.shared.space_x.RocketLaunchDTOMapper
import com.remote_first.shared.space_x.SpaceEffectUseCase
import com.remote_first.shared.space_x.SpaceXApi
import com.remote_first.shared.space_x.SpaceXRepo
import com.squareup.sqldelight.db.SqlDriver
import comremotefirstsharedcache.AppDatabaseQueries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

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

    @Provides
    fun provideSpaceXRepo(
            database: Database,
            spaceXApi: SpaceXApi,
            rocketLaunchDTOMapper: RocketLaunchDTOMapper,
    ) = SpaceXRepo(database, spaceXApi, rocketLaunchDTOMapper)

    @Provides
    @FlowPreview
    @ExperimentalCoroutinesApi
    fun provideGetLaunchesUseCase(spaceXRepo: SpaceXRepo) = GetLaunchesUseCase(spaceXRepo)

    @Provides
    fun provideSpaceEffectUseCase() = SpaceEffectUseCase()

    @Provides
    fun provideEmptyUseCase() = EmptyUseCase()

    @Provides
    fun provideRocketLaunchDTOMapper() = RocketLaunchDTOMapper

    @Provides
    fun provideContextProvider(@ApplicationContext appContext: Context) = ContextProvider(appContext)

    @Provides
    fun provideMainActivityContract(): IMainActivityIntentFactory = MainActivityIntentFactory()

    @Provides
    @Singleton
    fun provideEventBus(): EventBus = EventBus()

    @Provides
    fun provideEventBusService(eventBus: EventBus): EventBusService = EventBusService(eventBus)
}
