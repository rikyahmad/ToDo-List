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

/**
 * Dagger Hilt module for providing repository dependencies.
 *
 * This Dagger module is responsible for providing dependencies related to the repository layer,
 * such as TaskDatabase, TaskDao, and TaskRepository.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides an instance of TaskDatabase.
     *
     * This method provides an instance of TaskDatabase by getting the database using the application context.
     *
     * @param context The application context.
     * @return An instance of TaskDatabase.
     */
    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        return TaskDatabase.getDatabase(context)
    }

    /**
     * Provides an instance of TaskDao.
     *
     * This method provides an instance of TaskDao by accessing the task DAO from the provided TaskDatabase.
     *
     * @param database The TaskDatabase instance.
     * @return An instance of TaskDao.
     */
    @Provides
    @Singleton
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }

    /**
     * Provides an instance of TaskRepository.
     *
     * This method provides an instance of TaskRepository by taking TaskDao as a parameter.
     *
     * @param taskDao The TaskDao instance.
     * @return An instance of TaskRepository.
     */
    @Provides
    @Singleton
    fun provideRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }
}
