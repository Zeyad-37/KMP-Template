package com.remote_first.shared

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual val httpClientEngine: HttpClientEngine = Js.create()
actual val host: String = "127.0.0.1"
