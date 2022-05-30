package com.example.tasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table WHERE deadline > :present")
    fun getAllFuture(present: Long) : List<Task>

    @Insert
    fun prepopulate(task: Task)

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("UPDATE task_table SET task_done = NOT task_done WHERE taskId = :taskId")
    suspend fun toggleTaskDone(taskId: Long)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table WHERE taskId = :key")
    fun get(key: Long): Flow<Task>

    @Query("SELECT * FROM task_table ORDER BY taskId ASC")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE task_done ORDER BY taskId ASC")
    fun getAllCompleted(): Flow<List<Task>>

    //DEADLINE RELATED QUERIES
    @Query("SELECT * FROM task_table WHERE deadline > :present AND NOT task_done ORDER BY deadline ASC")
    fun getAllFutureByDeadlineAsc(present: Long): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE deadline < :present AND deadline IS NOT 0 AND NOT task_done ORDER BY deadline ASC")
    fun getAllOverdueByDeadlineAsc(present: Long): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE deadline IS 0 AND NOT task_done ")
    fun getAllWithoutDeadline() : Flow<List<Task>>

    @Query("SELECT * FROM task_table where deadline > :present AND NOT task_done ORDER BY deadline ASC LIMIT 1")
    fun getNextDeadline(present: Long) : Flow<Task>

    //PRIORITY RELATED QUERIES
    @Query("SELECT * FROM task_table WHERE priority = 0 AND NOT task_done ")
    fun getNoPriority() : Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE priority = 1 AND NOT task_done ")
    fun getLowPriority() : Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE priority = 2 AND NOT task_done ")
    fun getMediumPriority() : Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE priority = 3 AND NOT task_done ")
    fun getHighPriority() : Flow<List<Task>>
}