package com.staygrateful.todolistapp.domain.usecase

import androidx.lifecycle.LiveData
import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Use case interface for home page-related operations.
 *
 * This interface defines the contract for home page-related operations,
 * such as retrieving, inserting, updating, and deleting tasks.
 */
interface HomepageUseCase {

    /**
     * LiveData representing the list of all tasks.
     */
    val allTasks: LiveData<List<Task>>

    /**
     * Retrieves all tasks from the repository.
     *
     * This method fetches all tasks from the repository based on the provided title filter.
     *
     * @param title The optional title filter to search tasks by.
     */
    suspend fun getAllTasks(title: String)

    /**
     * Get upcoming tasks based on the due date.
     *
     * This method retrieves a list of tasks where the due date is greater than the current time,
     * ordered by due date in ascending order.
     *
     * @param currentTimeMillis The current time in milliseconds used for comparison with due dates.
     * @return A Flow of List<Task> representing upcoming tasks.
     */
    fun getUpcomingTasks(currentTimeMillis: Long): Flow<List<Task>>

    /**
     * Retrieves a task by its ID from the repository.
     *
     * This method retrieves a task from the repository based on its ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return The task with the specified ID, or null if not found.
     */
    suspend fun getTaskById(taskId: Long): Task?

    suspend fun updateTaskCompletion(taskId: Long, completed: Boolean)

    /**
     * Inserts a new task into the repository.
     *
     * This method inserts a new task into the repository.
     *
     * @param task The task to insert.
     */
    suspend fun insertTask(task: Task)

    /**
     * Updates an existing task in the repository.
     *
     * This method updates an existing task in the repository.
     *
     * @param task The task to update.
     */
    suspend fun updateTask(task: Task)

    /**
     * Deletes a task from the repository.
     *
     * This method deletes a task from the repository.
     *
     * @param task The task to delete.
     */
    suspend fun deleteTask(task: Task)

    /**
     * Deletes a task by its ID from the repository.
     *
     * This method deletes a task from the repository based on its ID.
     *
     * @param taskId The ID of the task to delete.
     */
    suspend fun deleteTaskById(taskId: Long)
}
