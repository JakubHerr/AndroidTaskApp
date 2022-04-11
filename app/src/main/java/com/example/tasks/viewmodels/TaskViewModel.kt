package com.example.tasks.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import com.example.tasks.other.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor (private val dao: TaskDao) : ViewModel() {
    val tasks = MediatorLiveData<List<Task>>()

    private val sortedByDeadline = dao.getAllByDeadlineAsc()
    private val sortedByPriority = dao.getAllByPriorityDesc()
    private val sortedById = dao.getAllByIdAsc()

    var sortedBy = SortType.DEFAULT

    init {
        tasks.addSource(sortedByDeadline) { result ->
            if(sortedBy == SortType.DEADLINE) {
                result?.let { tasks.value = it }
            }
        }
        tasks.addSource(sortedByPriority) { result ->
            if(sortedBy == SortType.PRIORITY) {
                result?.let { tasks.value = it }
            }
        }
        tasks.addSource(sortedById) { result ->
            if(sortedBy == SortType.DEFAULT) {
                result?.let { tasks.value = it }
            }
        }
    }

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

    fun sortBy(sortType: SortType) = when(sortType) {
        SortType.DEADLINE -> sortedByDeadline.value?.let { tasks.value = it }
        SortType.PRIORITY -> sortedByPriority.value?.let { tasks.value = it }
        SortType.DEFAULT -> sortedById.value?.let { tasks.value = it }
    }.also {
        this.sortedBy = sortType
    }
}