package com.example.tasks.viewmodels

import androidx.fragment.app.FragmentManager
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
    var newTaskName = ""
    private var date: Long? = null //Epoch milliseconds
    val tasks = dao.getAll()
    var task: MutableLiveData<Task> = MutableLiveData<Task>()

    fun addTask() {
        if(newTaskName.isBlank()) newTaskName = "Untitled"
        viewModelScope.launch {
            dao.insert(Task(taskName = newTaskName, date = date, taskDone = false))
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            dao.delete(task.value!!)
        }
    }

    fun editTask() {
        viewModelScope.launch {
            dao.update(task.value!!)
        }
    }

    fun loadTask(id: Long) {
        viewModelScope.launch {
            val test = dao.get(id)
            test?.let {
                task.value = it
            }
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