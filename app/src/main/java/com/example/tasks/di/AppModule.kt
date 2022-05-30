package com.example.tasks.di

import android.content.Context
import com.example.tasks.data.AppDatabase
import com.example.tasks.data.TaskDao
import com.example.tasks.data.repository.TaskRepository
import com.example.tasks.data.repository.TaskRepositoryImpl
import com.example.tasks.ui.viewmodel.TaskAddViewModel
import com.example.tasks.ui.viewmodel.TaskEditViewModel
import com.example.tasks.ui.viewmodel.TaskListViewModel
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
        ) = AppDatabase.getInstance(app)

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase) = db.taskDao

    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao) : TaskRepository = TaskRepositoryImpl(dao)
}