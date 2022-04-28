package com.example.tasks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*
import java.util.concurrent.Executors

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao

    //static functions for pre-populating a new empty database with mock data
    //more info: https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "tasks_database")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadExecutor().execute {
                            val dao = getInstance(context).taskDao
                            val time = Calendar.getInstance()

                            time.add(Calendar.DAY_OF_MONTH,-1) //Overdue task
                            dao.prepopulate(Task(taskId = 1, taskName = "task1", deadline = time, priority = 0))

                            time.add(Calendar.DAY_OF_MONTH,2) //Future task
                            dao.prepopulate(Task(taskId = 2, taskName = "task2", deadline = time, priority = 1))
                            time.add(Calendar.DAY_OF_MONTH,1)
                            dao.prepopulate(Task(taskId = 3, taskName = "task3", deadline = time, priority = 3))

                            time.timeInMillis = 0L //Task with no deadline
                            dao.prepopulate(Task(taskId = 4, taskName = "task4", deadline = time, priority = 2))

                            //Completed task
                            dao.prepopulate(Task(taskId = 5, taskName = "completed task", deadline = time, taskDone = true, priority = 3))
                        }
                    }
                })
                .build()
    }
}