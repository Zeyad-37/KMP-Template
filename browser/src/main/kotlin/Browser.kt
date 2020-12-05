package com.example.multiplatform.js

import com.remote_first.shared.hello
import kotlinx.browser.document

fun main() {
    hello {
        document.body!!.textContent = it
    }
}
