package com.staygrateful.todolistapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.staygrateful.todolistapp.data.local.dao.TaskDao
import com.staygrateful.todolistapp.data.model.Task

/**
 * Room database class for managing tasks data.
 *
 * This class represents the Room database for managing tasks data.
 * It provides access to the TaskDao for performing database operations.
 *
 * @see TaskDao
 */
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    /**
     * Abstract method to retrieve the TaskDao instance.
     *
     * This method returns the TaskDao instance, which provides methods for interacting with tasks data.
     *
     * @return The TaskDao instance.
     */
    abstract fun taskDao(): TaskDao

    /**
     * Companion object containing singleton instance of the TaskDatabase.
     */
    companion object {

        // Volatile variable to ensure visibility of changes to other threads
        @Volatile
        private var database: TaskDatabase? = null

        /**
         * Gets the singleton instance of TaskDatabase.
         *
         * This method returns the singleton instance of TaskDatabase.
         * If the instance is not yet initialized, it creates a new one.
         *
         * @param context The application context.
         * @return The singleton instance of TaskDatabase.
         */
        fun getDatabase(context: Context): TaskDatabase {
            return database ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).build()
                database = instance
                instance
            }
        }
    }
}
