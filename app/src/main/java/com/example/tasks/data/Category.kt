package com.example.tasks.data

import kotlinx.coroutines.flow.Flow

data class Category(
    var name: String = "",
    val tasks: Flow<List<Task>>
)