package com.remote_first.shared.flow_redux

enum class InputStrategy(val interval: Long) {
    NONE(0L), THROTTLE(200L), DEBOUNCE(500L)
}
