package com.example.tasks.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskAddViewModel @Inject constructor(private val dao: TaskDao): ViewModel() {
    fun addTask(task: Task) {
        Log.d("TaskViewModel", "Added task with timestamp ${task.deadline}")
        if (task.taskName.isBlank()) task.taskName = "Untitled"
        viewModelScope.launch {
            dao.insert(task)
        }
    }
}