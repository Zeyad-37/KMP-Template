package com.remote_first.shared.space_x

import com.remote_first.shared_network.RocketLaunchDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class SpaceXApi @Inject constructor(private val httpClient: HttpClient) {

    companion object {
        private const val LAUNCHES_ENDPOINT = "https://api.spacexdata.com/v3/launches"
    }

    suspend fun getAllLaunches(): List<RocketLaunchDTO> = httpClient.get(LAUNCHES_ENDPOINT)
}
