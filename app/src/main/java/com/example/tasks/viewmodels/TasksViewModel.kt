package com.example.tasks.viewmodels

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch

class TasksViewModel(private val dao: TaskDao) : ViewModel() {
    var newTaskName = "" //holds name for a new task

    val date: LiveData<Long?> get() = _date
    private val _date = MutableLiveData<Long?>(null) //Epoch milliseconds

    val tasks = dao.getAll() //holds list of all tasks

    var task = MutableLiveData<Task>() //holds task to be edited/deleted

    val navigateBack: LiveData<Boolean> get() = _navigateBack
    private val _navigateBack = MutableLiveData(false) //tells fragments when to return

    fun onNavigateBack() {
        _navigateBack.value = false
    }

    fun addTask() {
        if(newTaskName.isBlank()) newTaskName = "Untitled"
        viewModelScope.launch {
            dao.insert(Task(taskName = newTaskName, date = _date.value, taskDone = false))
        }
        _navigateBack.value = true
    }

    fun deleteTask() {
        viewModelScope.launch {
            dao.delete(task.value!!)
        }
        _navigateBack.value = true
    }

    fun editTask() {
        task.value?.date = _date.value
        if(task.value!!.taskName.isBlank()) task.value!!.taskName = "Untitled"
        viewModelScope.launch {
            dao.update(task.value!!)
        }
        _navigateBack.value = true
    }

    fun loadTask(id: Long) {
        viewModelScope.launch {
            val test = dao.get(id)
            test?.let {
                task.value = it
                _date.value = it.date
            }
        }
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

    fun getTime(fm: FragmentManager) {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .build()
        timePicker.show(fm,"timePicker")
        timePicker.addOnPositiveButtonClickListener {
            //TODO add time picking (works only if date is set)
        }
    }
}