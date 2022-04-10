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
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor (private val dao: TaskDao) : ViewModel() {
    var task = MutableLiveData<Task>() //holds task to be edited/deleted

    val date: LiveData<String> get() = _date
    private var _date = MutableLiveData("No date")

    val navigateBack: LiveData<Boolean> get() = _navigateBack
    private val _navigateBack = MutableLiveData(false) //tells fragments when to return

    fun onNavigateBack() {
        _navigateBack.value = false
    }

    fun deleteTask() {
        viewModelScope.launch {
            dao.delete(task.value!!)
        }
        _navigateBack.value = true
    }

    fun editTask() {
        if(task.value!!.taskName.isBlank()) task.value!!.taskName = "Untitled"
        viewModelScope.launch {
            dao.update(task.value!!)
        }
        _navigateBack.value = true
    }

    //TODO figure out a better solution
    // UI loads before task is fetched and binded to layout, causing flickering
    fun loadTask(id: Long) {
        viewModelScope.launch {
            val test = dao.get(id)
            test?.let {
                task.value = it
                _date.value = dateToString()
            }
        }
    }

    fun setDate(fm: FragmentManager) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Choose date")
            .setSelection(task.value?.date ?: MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.show(fm,"datePicker")
        datePicker.addOnPositiveButtonClickListener {
            task.value?.date = datePicker.selection
            _date.value = dateToString()
        }
        datePicker.addOnNegativeButtonClickListener {
            task.value?.date = null
            _date.value = dateToString()
        }
    }

    private fun dateToString() : String {
        var result = "No date"
        task.value?.date?.let {
            result = SimpleDateFormat("dd/MM/yyyy").format(it)
        }
        return result
    }
}