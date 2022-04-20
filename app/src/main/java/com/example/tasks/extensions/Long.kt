package com.example.tasks.extensions

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toDeadline() : String {
    return when(this) {
        0L -> "No deadline"
        else -> SimpleDateFormat("MMM dd HH:mm", Locale.US).format(this)
    }
}
