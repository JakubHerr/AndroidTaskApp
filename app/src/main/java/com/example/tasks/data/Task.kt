package com.example.tasks.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,

    @ColumnInfo(name = "task_name")
    var taskName: String = "",

    var deadline: LocalDateTime? = null,

    @ColumnInfo(name = "timezone")
    var timezone: TimeZone = TimeZone.UTC,

    @ColumnInfo(name = "time_estimate")
    var timeEstimate: Int = 0,

    @ColumnInfo(name = "task_done")
    var taskDone: Boolean = false,

    @ColumnInfo(name = "priority")
    var priority: Byte = 0
)
