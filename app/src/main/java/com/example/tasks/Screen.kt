package com.example.tasks

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class Screen(val route: String, @StringRes val resourceId: Int, @DrawableRes val iconId: Int?) {
    object TaskList: Screen("TaskList", R.string.task_list, R.drawable.ic_baseline_library_add_check_24)
    object AddTask: Screen("AddTask", R.string.add_task, null)
    object EditTask: Screen("EditTask", R.string.edit_task, null)
    object Calendar: Screen("Calendar",R.string.calendar, R.drawable.ic_baseline_calendar_month_24)
    object Overview: Screen("Overview", R.string.stats, R.drawable.ic_baseline_format_list_numbered_24)
}
