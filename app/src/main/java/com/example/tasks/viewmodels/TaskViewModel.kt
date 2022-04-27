package com.example.tasks.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    //TODO deadline based sorts should be updated periodically to stay relevant
    private val sortedByFutureDeadline = dao.getAllFutureByDeadlineAsc(Clock.System.now().toEpochMilliseconds())
    private val sortedByOverdueDeadline = dao.getAllOverdueByDeadlineAsc(Clock.System.now().toEpochMilliseconds())
    private val noDeadline = dao.getAllWithoutDeadline()

    private val completed = Category("Completed",dao.getAllCompleted())

    //reminder: do NOT mutate lists passed to DiffUtil, create a new list instead
    private val deadline = listOf(
        Category("Overdue",sortedByOverdueDeadline),
        Category("Future",sortedByFutureDeadline),
        Category("No date",noDeadline))

    private val deadlineWithCompleted = listOf(
        Category("Overdue",sortedByOverdueDeadline),
        Category("Future",sortedByFutureDeadline),
        Category("No date",noDeadline),
        completed)

    private val priority = listOf(
        Category("High",dao.getHighPriority()),
        Category("Medium",dao.getMediumPriority()),
        Category("Low",dao.getLowPriority()),
        Category("No", dao.getNoPriority())
    )

    private val priorityWithCompleted = listOf(
        Category("High",dao.getHighPriority()),
        Category("Medium",dao.getMediumPriority()),
        Category("Low",dao.getLowPriority()),
        Category("No", dao.getNoPriority()),
        completed
    )

    val categories = MutableLiveData(deadline)

    fun toggleCompleted() {
        categories.value = when(categories.value) {
            priority -> priorityWithCompleted
            priorityWithCompleted -> priority
            deadline -> deadlineWithCompleted
            deadlineWithCompleted -> deadline
            else -> null
        }
    }

    fun sortByPriority() {
        categories.value = when(categories.value) {
            deadlineWithCompleted -> priorityWithCompleted
            deadline -> priority
            else -> null
        }
    }

    fun sortByDeadline() {
        categories.value = when(categories.value) {
            priorityWithCompleted -> deadlineWithCompleted
            priority -> deadline
            else -> null
        }
    }

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