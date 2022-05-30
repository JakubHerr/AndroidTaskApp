package com.example.tasks.data.repository

import com.example.tasks.data.Category
import com.example.tasks.data.Task
import com.example.tasks.data.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val dao: TaskDao) : TaskRepository {
    private val now = Clock.System.now().toEpochMilliseconds()

    private val deadlineCategories = MutableStateFlow(mutableListOf(
        Category("Overdue", dao.getAllOverdueByDeadlineAsc(now)),
        Category("Future", dao.getAllFutureByDeadlineAsc(now)),
        Category("No deadline", dao.getAllWithoutDeadline()),
    ))

    private val priorityCategories = MutableStateFlow(mutableListOf(
        Category("High", dao.getHighPriority()),
        Category("Medium", dao.getMediumPriority()),
        Category("Low", dao.getLowPriority()),
        Category("No", dao.getNoPriority()),
    ))

    private val completed = Category("Completed", dao.getAllCompleted())

    private val selectedCategory = deadlineCategories

    private val mutex = Mutex()

    override suspend fun addTask(task: Task) {
        dao.insert(task)
    }

    override suspend fun editTask(task: Task) {
        dao.update(task)
    }

    override suspend fun toggleTaskCompleted(id: Long) {
        dao.toggleTaskDone(id)
    }

    override suspend fun deleteTask(task: Task) {
        dao.delete(task)
    }

    override fun getTask(id: Long): Flow<Task> = dao.get(id)

    override fun getAllTasks(): Flow<List<Task>> = dao.getAll()

    override fun getCategories(): StateFlow<List<Category>> = selectedCategory

    override suspend fun toggleCompletedCategory() {
        mutex.withLock {
            val categories = selectedCategory.value.toMutableList()
            categories.addOrDelete(completed)
            selectedCategory.value = categories
        }
    }
}

private fun MutableList<Category>.addOrDelete(newCategory: Category) {
    this.forEach { category ->
        if (category.name == newCategory.name) {
            this.remove(category)
            return
        }
    }
    this.add(newCategory)
}
