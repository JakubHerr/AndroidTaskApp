package com.example.tasks.di

import android.content.Context
import androidx.room.Room
import com.example.tasks.data.AppDatabase
import com.example.tasks.data.TaskDao
import com.example.tasks.viewmodels.TasksViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext app: Context
        ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "tasks_database"
    ).build()

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase) = db.taskDao

    @Provides
    @Singleton
    fun provideTasksViewModelFactory(dao: TaskDao) = TasksViewModelFactory(dao)
}