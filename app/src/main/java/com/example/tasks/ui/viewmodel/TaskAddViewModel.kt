package com.example.tasks.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import com.example.tasks.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskAddViewModel @Inject constructor(private val repository: TaskRepository): ViewModel() {
    fun addTask(task: Task) {
        if (task.taskName.isBlank()) task.taskName = "Untitled"
        viewModelScope.launch {
            repository.addTask(task)
        }
    }
}