package com.remote_first.shared.flow_redux

interface Output

expect interface State : Output

interface Outcome

interface Result : Outcome

data class Error(val message: String, val cause: Throwable, val input: Input? = null) : Output

interface Effect : Outcome, Output

open class Input(val showProgress: Boolean = true)

data class Progress(val isLoading: Boolean, val input: Input) : Output
