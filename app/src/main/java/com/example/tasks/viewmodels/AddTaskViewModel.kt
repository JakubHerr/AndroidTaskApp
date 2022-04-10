package com.example.tasks.viewmodels

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(private val dao: TaskDao) : ViewModel() {
    var newTaskName = "" //holds name for a new task

    val date: LiveData<Long?> get() = _date
    private val _date = MutableLiveData<Long?>(null) //Epoch milliseconds

    val navigateBack: LiveData<Boolean> get() = _navigateBack
    private val _navigateBack = MutableLiveData(false) //tells fragments when to return

    fun addTask() {
        if(newTaskName.isBlank()) newTaskName = "Untitled"
        viewModelScope.launch {
            dao.insert(Task(taskName = newTaskName, date = _date.value, taskDone = false))
        }
        _navigateBack.value = true
    }

    fun setDate(fm: FragmentManager) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Choose date")
            .setSelection(_date.value ?: MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.show(fm,"datePicker")
        datePicker.addOnPositiveButtonClickListener {
            _date.value = datePicker.selection
        }
        datePicker.addOnNegativeButtonClickListener {
            _date.value = null
        }
    }

    fun onNavigateBack() {
        _navigateBack.value = false
    }
}