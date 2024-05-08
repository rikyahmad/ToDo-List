package com.staygrateful.todolistapp.domain.usecase

import androidx.lifecycle.LiveData
import com.staygrateful.todolistapp.data.model.Task

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
     * Retrieves a task by its ID from the repository.
     *
     * This method retrieves a task from the repository based on its ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return The task with the specified ID, or null if not found.
     */
    suspend fun getTaskById(taskId: Long): Task?

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
