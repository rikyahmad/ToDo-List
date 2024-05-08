package com.staygrateful.todolistapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for managing Task entities.
 *
 * This interface provides methods for performing CRUD operations on Task entities in the local database.
 */
@Dao
interface TaskDao {

    /**
     * Retrieves all tasks from the database based on a title filter.
     *
     * This method retrieves all tasks from the database that match the provided title filter.
     *
     * @param title The title filter to search tasks by.
     * @return A list of tasks matching the title filter.
     */
    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :title || '%' ORDER BY dueDate ASC")
    suspend fun getAllTasks(title: String): List<Task>

    /**
     * Get upcoming tasks based on the due date.
     *
     * This method retrieves a list of tasks where the due date is greater than the current time,
     * ordered by due date in ascending order.
     *
     * @param currentTimeMillis The current time in milliseconds used for comparison with due dates.
     * @return A Flow of List<Task> representing upcoming tasks.
     */
    @Query("SELECT * FROM tasks WHERE dueDate > :currentTimeMillis ORDER BY dueDate ASC")
    fun getUpcomingTasks(currentTimeMillis: Long): Flow<List<Task>>

    /**
     * Retrieves a task by its ID from the database.
     *
     * This method retrieves a task from the database based on its ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return The task with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): Task?

    @Query("UPDATE tasks SET isCompleted = :completed WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Long, completed: Boolean)

    /**
     * Inserts a task into the database.
     *
     * This method inserts a new task into the database.
     *
     * @param task The task to insert.
     */
    @Insert
    suspend fun insertTask(task: Task)

    /**
     * Updates a task in the database.
     *
     * This method updates an existing task in the database.
     *
     * @param task The task to update.
     */
    @Update
    suspend fun updateTask(task: Task)

    /**
     * Deletes a task from the database.
     *
     * This method deletes a task from the database.
     *
     * @param task The task to delete.
     */
    @Delete
    suspend fun deleteTask(task: Task)

    /**
     * Deletes a task from the database by its ID.
     *
     * This method deletes a task from the database based on its ID.
     *
     * @param taskId The ID of the task to delete.
     * @return The number of rows affected by the deletion operation.
     */
    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long): Int

    /**
     * Deletes all tasks from the database.
     *
     * This method deletes all tasks from the database.
     */
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}
