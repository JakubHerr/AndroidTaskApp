package com.example.tasks.di

import android.content.Context
import com.example.tasks.data.AppDatabase
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
}