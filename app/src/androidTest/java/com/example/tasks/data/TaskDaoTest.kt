package com.example.tasks.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.tasks.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class TaskDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var taskDao: TaskDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() //prevents test from being stuck

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        taskDao = database.taskDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTask() = runBlockingTest {
        val taskId = 30L
        val task = Task(taskId = taskId, taskName = "test", date = 1234L, taskDone = true)
        taskDao.insert(task)

        val retrievedTask = taskDao.get(taskId).getOrAwaitValue()
        assertThat(retrievedTask).isEqualTo(task)
    }

    @Test
    fun updateTask() = runBlockingTest {
        val taskId = 10L
        val oldTask = Task(taskId, taskName = "old task")
        taskDao.insert(oldTask)

        val newTask = Task(taskId, taskName = "new task")
        taskDao.update(newTask)

        val savedTask = taskDao.get(taskId).getOrAwaitValue()
        assertThat(savedTask).isEqualTo(newTask)
    }

    @Test
    fun deleteTask() = runBlockingTest {
        val taskId = 30L
        val task = Task(taskId = taskId, taskName = "test", date = 1234L, taskDone = true)
        taskDao.insert(task)
        taskDao.delete(task)
        val retrievedTask = taskDao.get(taskId).getOrAwaitValue()
        assertThat(retrievedTask).isNull()
    }

    @Test
    fun getAllTasks() = runBlockingTest {
        val tasks = listOf(
            Task(taskId = 1, taskName = "task1"),
            Task(taskId = 2, taskName = "task2"),
            Task(taskId = 3, taskName = "task3"),
            Task(taskId = 4, taskName = "task4"))
        taskDao.insert(tasks[0])
        taskDao.insert(tasks[1])
        taskDao.insert(tasks[2])
        taskDao.insert(tasks[3])

        val retrievedTasks = taskDao.getAll().getOrAwaitValue()
        assertThat(retrievedTasks).isEqualTo(tasks)
        assertThat(retrievedTasks.size).isEqualTo(tasks.size)
    }

    @Test
    fun getValidTask() = runBlockingTest {
        taskDao.insert(Task(taskId = 123,taskName = "valid task"))
        assertThat(taskDao.get(123).getOrAwaitValue()).isNotNull()
    }

    @Test
    fun getInvalidTask() = runBlockingTest {
        assertThat(taskDao.get(123).getOrAwaitValue()).isNull()
    }
}