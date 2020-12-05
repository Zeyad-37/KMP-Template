package com.remote_first.shared.db

import com.remote_first.shared.space_x.data_transfer_objects.LinksDTO
import com.remote_first.shared.space_x.data_transfer_objects.RocketDTO
import com.remote_first.shared.space_x.data_transfer_objects.RocketLaunchDTO
import comremotefirstsharedcache.AppDatabaseQueries
import javax.inject.Inject

class Database @Inject constructor(val dbQuery: AppDatabaseQueries) {

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllRockets()
            dbQuery.removeAllLaunches()
        }
    }

    internal fun getAllLaunches(): List<RocketLaunchDTO> {
        return dbQuery.selectAllLaunchesInfo(::mapLaunchSelecting).executeAsList()
    }

    private fun mapLaunchSelecting(
            flightNumber: Long,
            missionName: String,
            launchYear: Int,
            rocketId: String,
            details: String?,
            launchSuccess: Boolean?,
            launchDateUTC: String,
            missionPatchUrl: String?,
            articleUrl: String?,
            rocket_id: String?,
            name: String?,
            type: String?
    ): RocketLaunchDTO {
        return RocketLaunchDTO(
                flightNumber = flightNumber.toInt(),
                missionName = missionName,
                launchYear = launchYear,
                details = details,
                launchDateUTC = launchDateUTC,
                launchSuccess = launchSuccess,
                rocket = RocketDTO(
                        id = rocketId,
                        name = name!!,
                        type = type!!
                ),
                links = LinksDTO(
                        missionPatchUrl = missionPatchUrl,
                        articleUrl = articleUrl
                )
        )
    }

    internal fun createLaunches(launches: List<RocketLaunchDTO>) {
        dbQuery.transaction {
            launches.forEach { launch ->
                val rocket = dbQuery.selectRocketById(launch.rocket.id).executeAsOneOrNull()
                if (rocket == null) {
                    insertRocket(launch)
                }

                insertLaunch(launch)
            }
        }
    }

    private fun insertRocket(launch: RocketLaunchDTO) {
        dbQuery.insertRocket(
                id = launch.rocket.id,
                name = launch.rocket.name,
                type = launch.rocket.type
        )
    }

    private fun insertLaunch(launch: RocketLaunchDTO) {
        dbQuery.insertLaunch(
                flightNumber = launch.flightNumber.toLong(),
                missionName = launch.missionName,
                launchYear = launch.launchYear,
                rocketId = launch.rocket.id,
                details = launch.details,
                launchSuccess = launch.launchSuccess ?: false,
                launchDateUTC = launch.launchDateUTC,
                missionPatchUrl = launch.links.missionPatchUrl,
                articleUrl = launch.links.articleUrl
        )
    }
}