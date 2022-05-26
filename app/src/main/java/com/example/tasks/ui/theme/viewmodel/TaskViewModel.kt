package com.example.tasks.ui.theme.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Category
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val dao: TaskDao) : ViewModel() {
    private var sortedByFutureDeadline =
        dao.getAllFutureByDeadlineAsc(Clock.System.now().toEpochMilliseconds())
    private var sortedByOverdueDeadline =
        dao.getAllOverdueByDeadlineAsc(Clock.System.now().toEpochMilliseconds())
    private val noDeadline = dao.getAllWithoutDeadline()

    private val overdue = MutableStateFlow(emptyList<Task>())
    private val future = MutableStateFlow(emptyList<Task>())
    private val noTime = MutableStateFlow(emptyList<Task>())

    private val highPriority = MutableStateFlow(emptyList<Task>())
    private val mediumPriority = MutableStateFlow(emptyList<Task>())
    private val lowPriority = MutableStateFlow(emptyList<Task>())
    private val noPriority = MutableStateFlow(emptyList<Task>())

    private val completed = MutableStateFlow(emptyList<Task>())

    init {
        viewModelScope.launch {
            sortedByOverdueDeadline.collect {
                overdue.value = it
            }
        }
        viewModelScope.launch {
            sortedByFutureDeadline.collect {
                future.value = it
            }
        }
        viewModelScope.launch {
            noDeadline.collect {
                noTime.value = it
            }
        }
        viewModelScope.launch {
            dao.getHighPriority().collect {
                highPriority.value = it
            }
        }
        viewModelScope.launch {
            dao.getMediumPriority().collect {
                mediumPriority.value = it
            }
        }
        viewModelScope.launch {
            dao.getLowPriority().collect {
                lowPriority.value = it
            }
        }
        viewModelScope.launch {
            dao.getNoPriority().collect {
                noPriority.value = it
            }
        }
        viewModelScope.launch {
            dao.getAllCompleted().collect {
                completed.value = it
            }
        }
    }

    //reminder: do NOT mutate lists passed to DiffUtil, create a new list instead
    private var deadline = mutableListOf(
        Category("Overdue", overdue),
        Category("Future", future),
        Category("No date", noTime)
    )

    private var priority = mutableListOf(
        Category("High",highPriority),
        Category("Medium",mediumPriority),
        Category("Low",lowPriority),
        Category("No",noPriority),
    )


    val categories = MutableStateFlow(deadline)

    fun setPriorityCategory() {
        categories.value = priority
    }

    fun setDeadlineCategory() {
        categories.value = deadline
    }

    private var showingCompleted = false

    fun toggleCompleted() {
        showingCompleted = !showingCompleted

        if (showingCompleted) {
            priority.add(Category("Completed", completed))
            deadline.add(Category("Completed", completed))
        }
        else {
            priority.remove(Category("Completed", completed))
            deadline.remove(Category("Completed", completed))
        }
        categories.update { categories.value }
    }

    fun addTask(task: Task) {
        Log.d("TaskViewModel", "Added task with timestamp ${task.deadline}")
        if (task.taskName.isBlank()) task.taskName = "Untitled"
        viewModelScope.launch {
            dao.insert(task)
        }
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

    fun completeTask(taskId: Long) {
        viewModelScope.launch {
            dao.toggleTaskDone(taskId)
        }
    }

    fun retrieveTask(taskId: Long): Flow<Task> {
        return dao.get(taskId)
    }

    fun retrieveNextTask(present: Long): Flow<Task> {
        return dao.getNextDeadline(present)
    }
}