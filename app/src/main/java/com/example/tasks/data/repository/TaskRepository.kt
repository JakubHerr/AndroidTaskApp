package com.example.tasks.data.repository

import com.example.tasks.data.Category
import com.example.tasks.data.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TaskRepository {
    suspend fun addTask(task: Task)

    suspend fun editTask(task: Task)

    suspend fun toggleTaskCompleted(id: Long)

    suspend fun deleteTask(task: Task)

    fun getTask(id: Long) : Flow<Task>

    fun getAllTasks() : Flow<List<Task>>

    fun getCategories() : StateFlow<List<Category>>

    suspend fun toggleCompletedCategory()

    fun selectDeadlineCategory()

    fun selectPriorityCategory()
}