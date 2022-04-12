package com.example.tasks.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)
    @Update
    suspend fun update(task: Task)

    @Query("UPDATE task_table SET task_done = NOT task_done WHERE taskId = :taskId")
    suspend fun toggleTaskDone(taskId: Long)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table WHERE taskId = :key")
    fun get(key: Long): LiveData<Task>

    @Query("SELECT * FROM task_table ORDER BY taskId ASC")
    fun getAllByIdAsc(): LiveData<List<Task>>

    //get all tasks most important first
    @Query("SELECT * from task_table ORDER BY priority DESC")
    fun getAllByPriorityDesc(): LiveData<List<Task>>

    //DEADLINE RELATED QUERIES
    @Query("SELECT * FROM task_table WHERE date > :present ORDER BY date ASC")
    fun getAllFutureByDeadlineAsc(present: Long): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE date < :present AND date IS NOT 0 ORDER BY date ASC")
    fun getAllOverdueByDeadlineAsc(present: Long): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE date IS 0")
    fun getAllWithoutDeadline() : LiveData<List<Task>>


}