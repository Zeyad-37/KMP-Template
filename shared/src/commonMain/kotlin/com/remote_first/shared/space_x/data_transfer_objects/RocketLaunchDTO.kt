package com.remote_first.shared.space_x.data_transfer_objects

import com.remote_first.shared.MapsTo
import com.remote_first.shared.space_x.domain_objects.Links
import com.remote_first.shared.space_x.domain_objects.Rocket
import com.remote_first.shared.space_x.domain_objects.RocketLaunch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RocketLaunchDTO(
        @SerialName("flight_number")
        val flightNumber: Int,
        @SerialName("mission_name")
        val missionName: String,
        @SerialName("launch_year")
        val launchYear: Int,
        @SerialName("launch_date_utc")
        val launchDateUTC: String,
        @SerialName("rocket")
        val rocket: RocketDTO,
        @SerialName("details")
        val details: String?,
        @SerialName("launch_success")
        val launchSuccess: Boolean?,
        @SerialName("links")
        val links: LinksDTO,
) : MapsTo<RocketLaunch> {
        override fun map() = RocketLaunch(
                flightNumber,
                missionName,
                launchYear,
                launchDateUTC,
                rocket.map(),
                details,
                launchSuccess,
                links.map()
        )
}

@Serializable
data class RocketDTO(
        @SerialName("rocket_id")
        val id: String,
        @SerialName("rocket_name")
        val name: String,
        @SerialName("rocket_type")
        val type: String,
) : MapsTo<Rocket> {
        override fun map() = Rocket(id, name, type)
}

@Serializable
data class LinksDTO(
        @SerialName("mission_patch")
        val missionPatchUrl: String?,
        @SerialName("article_link")
        val articleUrl: String?,
) : MapsTo<Links> {
        override fun map() = Links(missionPatchUrl, articleUrl)
}
