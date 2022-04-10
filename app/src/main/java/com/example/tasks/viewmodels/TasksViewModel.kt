package com.example.tasks.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tasks.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor (dao: TaskDao) : ViewModel() {
    val tasks = dao.getAll() //holds list of all tasks
}