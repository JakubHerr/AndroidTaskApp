package com.example.tasks.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Category
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor (private val dao: TaskDao) : ViewModel() {
    private val sortedByFutureDeadline = dao.getAllFutureByDeadlineAsc(Clock.System.now().toEpochMilliseconds())
    private val sortedByOverdueDeadline = dao.getAllOverdueByDeadlineAsc(Clock.System.now().toEpochMilliseconds())
    private val noDeadline = dao.getAllWithoutDeadline()

    val categories = listOf(
        Category("Overdue",sortedByOverdueDeadline),
        Category("Future",sortedByFutureDeadline),
        Category("No date",noDeadline))

    fun addTask(task: Task) {
        if(task.taskName.isBlank()) task.taskName = "Untitled"
        viewModelScope.launch {
            dao.insert(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dao.delete(task)
        }
    }

    fun editTask(task: Task) {
        if(task.taskName.isBlank()) task.taskName = "Untitled"
        viewModelScope.launch {
            dao.update(task)
        }
    }

    fun completeTask(taskId: Long) {
        viewModelScope.launch {
            dao.toggleTaskDone(taskId)
        }
    }

    fun retrieveTask(taskId: Long) : LiveData<Task> {
        return dao.get(taskId)
    }
}