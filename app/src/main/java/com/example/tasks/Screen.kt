package com.example.tasks

sealed class Screen(val route: String) {
    object TaskList: Screen("TaskList")
    object Overview: Screen("Overview")
    object Calendar: Screen("Calendar")
}
