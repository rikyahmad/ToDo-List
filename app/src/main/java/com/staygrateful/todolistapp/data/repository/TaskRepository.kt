package com.staygrateful.todolistapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.staygrateful.todolistapp.data.local.dao.TaskDao
import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Repository class for managing tasks data.
 *
 * This class serves as an intermediary between the ViewModel and the local database (Room).
 * It provides methods to perform CRUD operations on tasks and manages the data flow to/from the database.
 *
 * @param taskDao The Data Access Object (DAO) for tasks.
 */
class TaskRepository(
    private val taskDao: TaskDao,
) {

    // LiveData to hold the list of all tasks
    private val _allTasks = MutableLiveData<List<Task>>()

    // Exposed LiveData for observing changes in the list of tasks
    val allTasks: LiveData<List<Task>> = _allTasks

    // Search filter to keep track of the current search query
    private var searchFilter: String = ""

    /**
     * Retrieves all tasks from the database.
     *
     * This method fetches all tasks from the database based on the provided title filter.
     * It updates the [_allTasks] LiveData with the retrieved list of tasks.
     *
     * @param title The optional title filter to search tasks by.
     */
    suspend fun getAllTasks(title: String) {
        _allTasks.value = taskDao.getAllTasks(title)
        searchFilter = title
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
    fun getUpcomingTasks(currentTimeMillis: Long): Flow<List<Task>> {
        return taskDao.getUpcomingTasks(currentTimeMillis)
    }

    /**
     * Retrieves a task by its ID from the database.
     *
     * This method retrieves a task from the database based on its ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return The task with the specified ID, or null if not found.
     */
    suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun updateTaskCompletion(taskId: Long, completed: Boolean) {
        taskDao.updateTaskCompletion(taskId, completed)
    }

    /**
     * Inserts a new task into the database.
     *
     * This method inserts a new task into the database and refreshes the list of tasks.
     *
     * @param task The task to insert.
     */
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
        getAllTasks(searchFilter)
    }

    /**
     * Updates an existing task in the database.
     *
     * This method updates an existing task in the database and refreshes the list of tasks.
     *
     * @param task The task to update.
     */
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
        getAllTasks(searchFilter)
    }

    /**
     * Deletes a task from the database.
     *
     * This method deletes a task from the database and refreshes the list of tasks.
     *
     * @param task The task to delete.
     */
    suspend fun deleteTask(task: Task) {
        // Use query to get the result row count
        deleteTaskById(task.id)
    }

    /**
     * Deletes a task by its ID from the database.
     *
     * This method deletes a task from the database based on its ID and refreshes the list of tasks.
     *
     * @param taskId The ID of the task to delete.
     */
    suspend fun deleteTaskById(taskId: Long) {
        taskDao.deleteTaskById(taskId).also { result ->
            if(result > 0) {
                getAllTasks(searchFilter)
            }
        }
    }

    /**
     * Deletes all tasks from the database.
     *
     * This method deletes all tasks from the database and refreshes the list of tasks.
     */
    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
        getAllTasks(searchFilter)
    }
}
