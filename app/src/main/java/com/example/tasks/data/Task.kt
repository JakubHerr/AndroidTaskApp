package com.example.tasks.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,

    @ColumnInfo(name = "task_name")
    var taskName: String = "",

    //TODO use LocalDateTime with timezone offset instead
    @ColumnInfo(name = "deadline")
    var deadline: Calendar = Calendar.getInstance().apply { timeInMillis = 0L },

    @ColumnInfo(name = "time_estimate")
    var timeEstimate: Int = 0,

    @ColumnInfo(name = "task_done")
    var taskDone: Boolean = false,

    @ColumnInfo(name = "priority")
    var priority: Byte = 0
)
