package com.example.tasks.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.text.SimpleDateFormat
import java.util.*

fun LocalDateTime.format(): String =
    SimpleDateFormat("MMM dd HH:mm", Locale.US)
        .format(this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())
