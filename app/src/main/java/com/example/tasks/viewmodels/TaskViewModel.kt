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

    fun retrieveTask(taskId: Long) : LiveData<Task> {
        return dao.get(taskId)
    }

//currently unused functions for instant sorting of single recyclerview
//val tasks = MediatorLiveData<List<Task>>()
//private var sortedBy = SortType.FUTURE_DEADLINE
//    init {
//        tasks.addSource(sortedByFutureDeadline) { result ->
//            if(sortedBy == SortType.FUTURE_DEADLINE) {
//                result?.let { tasks.value = it }
//            }
//        }
//        tasks.addSource(sortedByOverdueDeadline) { result ->
//            if(sortedBy == SortType.OVERDUE_DEADLINE) {
//                result?.let { tasks.value = it }
//            }
//        }
//        tasks.addSource(sortedByPriority) { result ->
//            if(sortedBy == SortType.PRIORITY) {
//                result?.let { tasks.value = it }
//            }
//        }
//        tasks.addSource(sortedById) { result ->
//            if(sortedBy == SortType.DEFAULT) {
//                result?.let { tasks.value = it }
//            }
//        }
//    }

//    fun sortBy(sortType: SortType) = when(sortType) {
//        SortType.FUTURE_DEADLINE -> sortedByFutureDeadline.value?.let { tasks.value = it }
//        SortType.OVERDUE_DEADLINE -> sortedByOverdueDeadline.value?.let { tasks.value = it }
//        SortType.PRIORITY -> sortedByPriority.value?.let { tasks.value = it }
//        SortType.DEFAULT -> sortedById.value?.let { tasks.value = it }
//    }.also {
//        this.sortedBy = sortType
//    }
}