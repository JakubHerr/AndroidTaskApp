package com.example.tasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE deadline > :present")
    fun getAllFuture(present: LocalDateTime) : List<Task>

    @Insert
    fun prepopulate(task: Task)

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("UPDATE task SET task_done = NOT task_done WHERE taskId = :taskId")
    suspend fun toggleTaskDone(taskId: Long)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task WHERE taskId = :key")
    fun get(key: Long): Flow<Task>

    @Query("SELECT * FROM task ORDER BY taskId ASC")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE task_done ORDER BY taskId ASC")
    fun getAllCompleted(): Flow<List<Task>>

    //DEADLINE RELATED QUERIES
    @Query("SELECT * FROM task WHERE deadline > :present AND NOT task_done ORDER BY deadline ASC")
    fun getAllFutureByDeadlineAsc(present: LocalDateTime): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE deadline < :present AND deadline IS NOT 0 AND NOT task_done ORDER BY deadline ASC")
    fun getAllOverdueByDeadlineAsc(present: LocalDateTime): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE deadline IS NULL AND NOT task_done ")
    fun getAllWithoutDeadline() : Flow<List<Task>>

    @Query("SELECT * FROM task where deadline > :present AND NOT task_done ORDER BY deadline ASC LIMIT 1")
    fun getNextDeadline(present: LocalDateTime) : Flow<Task>

    //PRIORITY RELATED QUERIES
    @Query("SELECT * FROM task WHERE priority = 0 AND NOT task_done ")
    fun getNoPriority() : Flow<List<Task>>

    @Query("SELECT * FROM task WHERE priority = 1 AND NOT task_done ")
    fun getLowPriority() : Flow<List<Task>>

    @Query("SELECT * FROM task WHERE priority = 2 AND NOT task_done ")
    fun getMediumPriority() : Flow<List<Task>>

    @Query("SELECT * FROM task WHERE priority = 3 AND NOT task_done ")
    fun getHighPriority() : Flow<List<Task>>
}