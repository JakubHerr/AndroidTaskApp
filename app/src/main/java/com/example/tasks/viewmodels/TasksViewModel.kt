package com.example.tasks.viewmodels

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone

class TasksViewModel(val dao: TaskDao) : ViewModel() {
    var newTaskName = ""
    var date: Long? = null //Epoch milliseconds
    val tasks = dao.getAll()
    val tz = TimeZone.currentSystemDefault()

    fun addTask() {
        if(newTaskName.isBlank()) newTaskName = "Untitled"
        viewModelScope.launch {
            val task = Task()
            task.taskName = newTaskName
            task.date = date
            dao.insert(task)
        }
    }

    fun getDate(fm: FragmentManager) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Choose date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.show(fm,"datePicker")
        datePicker.addOnPositiveButtonClickListener {
            date = datePicker.selection
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