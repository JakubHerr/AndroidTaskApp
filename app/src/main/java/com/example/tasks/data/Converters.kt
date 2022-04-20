package com.example.tasks.data

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun calendarToTimestamp(calendar: Calendar) : Long = calendar.timeInMillis

    @TypeConverter
    fun timestampToCalendar(ts: Long) : Calendar = Calendar.getInstance().apply {
        timeInMillis = ts
    }
}