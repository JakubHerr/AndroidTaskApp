package com.example.tasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Category
import com.example.tasks.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    val categories = MutableStateFlow(emptyList<Category>())

    init {
        viewModelScope.launch {
            repository.getCategories().collect {
                categories.value = it
            }
        }
    }

    fun toggleTaskCompleted(taskId: Long) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(taskId)
        }
    }

    fun toggleCompletedCategory() {
        viewModelScope.launch {
            repository.toggleCompletedCategory()
        }
    }

    fun selectDeadlineCategory() {
        repository.selectDeadlineCategory()
    }

    fun selectPriorityCategory() {
        repository.selectPriorityCategory()
    }
}