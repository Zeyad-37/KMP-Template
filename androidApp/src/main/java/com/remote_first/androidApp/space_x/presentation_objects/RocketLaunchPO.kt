package com.remote_first.androidApp.space_x.presentation_objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RocketLaunchPO(
        val flightNumber: Int,
        val missionName: String,
        val launchYear: Int,
        val launchDateUTC: String,
        val rocket: RocketPO,
        val details: String?,
        val launchSuccess: Boolean?,
        val links: LinksPO,
) : Parcelable

@Parcelize
data class RocketPO(
        val id: String,
        val name: String,
        val type: String,
) : Parcelable

@Parcelize
data class LinksPO(
        val missionPatchUrl: String?,
        val articleUrl: String?,
) : Parcelable
