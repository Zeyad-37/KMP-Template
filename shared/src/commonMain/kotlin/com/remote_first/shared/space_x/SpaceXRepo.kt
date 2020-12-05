package com.remote_first.shared.space_x

import com.remote_first.shared.db.Database
import com.remote_first.shared.space_x.domain_objects.RocketLaunch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SpaceXRepo @Inject constructor(private val database: Database, private val api: SpaceXApi) {

    fun getLaunches(): Flow<List<RocketLaunch>> {
        return flow {
            val cachedLaunches = database.getAllLaunches()
            emit(if (cachedLaunches.isNotEmpty()) {
                cachedLaunches
            } else {
                api.getAllLaunches().also {
                    database.clearDatabase()
                    database.createLaunches(it)
                }
            }.map { it.map() })
        }
    }
}
