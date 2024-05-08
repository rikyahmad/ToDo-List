package com.staygrateful.todolistapp.domain.interactor

import androidx.lifecycle.LiveData
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.data.repository.TaskRepository
import com.staygrateful.todolistapp.domain.usecase.HomepageUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interactor class for handling home page-related use cases.
 *
 * This class implements the HomepageUseCase interface and serves as an intermediary between the ViewModel
 * and the TaskRepository. It provides methods to interact with tasks data for the home page.
 *
 * @param taskRepository The repository responsible for managing tasks data.
 */
class HomepageInteractor @Inject constructor(
    private val taskRepository: TaskRepository
) : HomepageUseCase {

    // LiveData to hold the list of all tasks
    override val allTasks: LiveData<List<Task>> = taskRepository.allTasks

    /**
     * Retrieves all tasks from the repository.
     *
     * This method fetches all tasks from the repository based on the provided title filter.
     *
     * @param title The optional title filter to search tasks by.
     */
    override suspend fun getAllTasks(title: String) {
        taskRepository.getAllTasks(title)
    }

    /**
     * Get upcoming tasks based on the due date.
     *
     * This method retrieves a list of tasks where the due date is greater than the current time,
     * ordered by due date in ascending order.
     *
     * @param currentTimeMillis The current time in milliseconds used for comparison with due dates.
     * @return A Flow of List<Task> representing upcoming tasks.
     */
    override fun getUpcomingTasks(currentTimeMillis: Long): Flow<List<Task>> {
        return taskRepository.getUpcomingTasks(currentTimeMillis)
    }

    /**
     * Retrieves a task by its ID from the repository.
     *
     * This method retrieves a task from the repository based on its ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return The task with the specified ID, or null if not found.
     */
    override suspend fun getTaskById(taskId: Long): Task? {
        return taskRepository.getTaskById(taskId)
    }

    override suspend fun updateTaskCompletion(taskId: Long, completed: Boolean) {
        taskRepository.updateTaskCompletion(taskId, completed)
    }

    /**
     * Inserts a new task into the repository.
     *
     * This method inserts a new task into the repository.
     *
     * @param task The task to insert.
     */
    override suspend fun insertTask(task: Task) {
        taskRepository.insertTask(task)
    }

    /**
     * Updates an existing task in the repository.
     *
     * This method updates an existing task in the repository.
     *
     * @param task The task to update.
     */
    override suspend fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }

    /**
     * Deletes a task from the repository.
     *
     * This method deletes a task from the repository.
     *
     * @param task The task to delete.
     */
    override suspend fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }

    /**
     * Deletes a task by its ID from the repository.
     *
     * This method deletes a task from the repository based on its ID.
     *
     * @param taskId The ID of the task to delete.
     */
    override suspend fun deleteTaskById(taskId: Long) {
        taskRepository.deleteTaskById(taskId)
    }
}
