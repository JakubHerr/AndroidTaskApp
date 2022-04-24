package com.example.tasks.viewmodels

import android.util.Log
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

    val deadline = mutableListOf(
        Category("Overdue",sortedByOverdueDeadline),
        Category("Future",sortedByFutureDeadline),
        Category("No date",noDeadline))

    val priority = listOf(
        Category("No", dao.getNoPriority()),
        Category("Low",dao.getLowPriority()),
        Category("Medium",dao.getMediumPriority()),
        Category("High",dao.getHighPriority())
    )

    val default = listOf(
        Category("Tasks",dao.getAll())
    )

    private val completed = Category("Completed",dao.getAllCompleted())

    fun addTask(task: Task) {
        Log.d("TaskViewModel","Added task with timestamp ${task.deadline}")
        if(task.taskName.isBlank()) task.taskName = "Untitled"
        viewModelScope.launch {
            dao.insert(task)
        }
    }

    fun deleteTask(task: Task) {
        Log.d("TaskViewModel","deleted task with id ${task.taskId}")
        viewModelScope.launch {
            dao.delete(task)
        }
    }

    fun editTask(task: Task) {
        Log.d("TaskViewModel","edited task with timestamp ${task.deadline}")
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

    fun retrieveNextTask(present: Long) : LiveData<Task> {
        return  dao.getNextDeadline(present)
    }
}