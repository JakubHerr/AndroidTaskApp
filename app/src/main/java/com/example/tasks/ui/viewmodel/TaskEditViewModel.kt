package com.example.tasks.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(private val dao: TaskDao) : ViewModel() {
    fun retrieveTask(taskId: Long): Flow<Task> {
        return dao.get(taskId)
    }

    fun deleteTask(task: Task) {
        Log.d("TaskViewModel", "deleted task with id ${task.taskId}")
        viewModelScope.launch {
            dao.delete(task)
        }
    }

    fun editTask(task: Task) {
        Log.d("TaskViewModel", "edited task with timestamp ${task.deadline}")
        if (task.taskName.isBlank()) task.taskName = "Untitled"
        viewModelScope.launch {
            dao.update(task)
        }
    }
}