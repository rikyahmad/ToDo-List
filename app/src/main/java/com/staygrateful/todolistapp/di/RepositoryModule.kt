package com.staygrateful.todolistapp.di

import android.content.Context
import com.staygrateful.todolistapp.data.local.dao.TaskDao
import com.staygrateful.todolistapp.data.local.database.TaskDatabase
import com.staygrateful.todolistapp.data.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        return TaskDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }
}