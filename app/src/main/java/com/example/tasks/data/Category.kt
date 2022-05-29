package com.example.tasks.data

import kotlinx.coroutines.flow.StateFlow

data class Category(
    var name: String = "",
    val tasks: StateFlow<List<Task>>
)