package com.remote_first.shared

import io.ktor.client.engine.HttpClientEngine

expect val httpClientEngine: HttpClientEngine
expect val host: String
