package com.example.tasks.data

import androidx.lifecycle.LiveData

data class Category(
    var name: String = "",
    val tasks: LiveData<List<Task>>
)