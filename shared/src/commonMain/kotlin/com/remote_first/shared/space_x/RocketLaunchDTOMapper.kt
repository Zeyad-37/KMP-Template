package com.remote_first.shared.space_x

import com.remote_first.shared.space_x.domain_objects.Links
import com.remote_first.shared.space_x.domain_objects.Rocket
import com.remote_first.shared.space_x.domain_objects.RocketLaunch
import com.remote_first.shared_network.LinksDTO
import com.remote_first.shared_network.RocketDTO
import com.remote_first.shared_network.RocketLaunchDTO

object RocketLaunchDTOMapper {
    fun map(rocketLaunchDTO: RocketLaunchDTO) = with(rocketLaunchDTO) {
        RocketLaunch(
                flightNumber,
                missionName,
                launchYear,
                launchDateUTC,
                map(rocket),
                details,
                launchSuccess,
                map(links)
        )
    }

    fun map(rocketDTO: RocketDTO) = with(rocketDTO) { Rocket(id, name, type) }
    fun map(linksDTO: LinksDTO) = with(linksDTO) { Links(missionPatchUrl, articleUrl) }
}