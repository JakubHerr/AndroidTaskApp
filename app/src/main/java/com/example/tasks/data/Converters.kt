package com.example.tasks.data

import androidx.room.TypeConverter
import kotlinx.datetime.*

class Converters {
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? = when(date) {
        null -> null
        else -> date.toString()
    }

    @TypeConverter
    fun toLocalDateTime(isoString: String?): LocalDateTime? = when(isoString) {
        null -> null
        else -> LocalDateTime.parse(isoString)
    }

    @TypeConverter
    fun fromTimeZone(tz: TimeZone): String = tz.id

    @TypeConverter
    fun toTimeZone(id: String): TimeZone = TimeZone.of(id)
}