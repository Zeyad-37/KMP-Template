package com.remote_first.shared.space_x.domain_objects

data class RocketLaunch(
        val flightNumber: Int,
        val missionName: String,
        val launchYear: Int,
        val launchDateUTC: String,
        val rocket: Rocket,
        val details: String?,
        val launchSuccess: Boolean?,
        val links: Links,
)

data class Rocket(
        val id: String,
        val name: String,
        val type: String,
)

data class Links(
        val missionPatchUrl: String?,
        val articleUrl: String?,
)

object SpaceEffectK
