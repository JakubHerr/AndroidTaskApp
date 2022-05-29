package com.example.tasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Category
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(private val dao: TaskDao) : ViewModel() {
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
    }

    fun completeTask(taskId: Long) {
        viewModelScope.launch {
            dao.toggleTaskDone(taskId)
        }
    }
}