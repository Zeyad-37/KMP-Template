package com.remote_first.shared.space_x

import com.remote_first.shared.db.Database
import com.remote_first.shared.space_x.domain_objects.RocketLaunch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SpaceXRepo(
        private val database: Database,
        private val api: SpaceXApi,
        private val rocketLaunchDTOMapper: RocketLaunchDTOMapper,
) {

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
            }.map { rocketLaunchDTOMapper.map(it) })
        }
    }
}
