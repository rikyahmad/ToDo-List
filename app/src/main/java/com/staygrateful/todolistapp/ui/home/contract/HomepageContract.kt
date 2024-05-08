package com.staygrateful.todolistapp.ui.home.contract

import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Contract interface for defining interactions between the View and Presenter in the Homepage feature.
 */
interface HomepageContract {
    /**
     * View interface for defining methods to be implemented by the View layer.
     */
    interface View {
        // Add methods to be implemented by the View layer if needed
    }

    /**
     * UserActionListener interface for defining methods to be implemented by the Presenter layer.
     * This interface handles user actions and communicates with the data layer.
     */
    interface UserActionListener {
        /**
         * Retrieves all tasks from the data layer.
         * Optionally filters tasks by title.
         * @param title Optional parameter to filter tasks by title.
         */
        fun getAllTasks(title: String = "")

        /**
         * Get upcoming tasks based on the due date.
         *
         * This method retrieves a list of tasks where the due date is greater than the current time,
         * ordered by due date in ascending order.
         *
         * @return A LiveData of List<Task> representing upcoming tasks.
         */
        fun getUpcomingTasks(): Flow<List<Task>>

        /**
         * Inserts a new task into the data layer.
         * @param task The task object to be inserted.
         */
        fun insertTask(task: Task)

        /**
         * Updates an existing task in the data layer.
         * @param task The task object to be updated.
         */
        fun updateTask(task: Task)

        /**
         * Deletes a task from the data layer.
         * @param task The task object to be deleted.
         */
        fun deleteTask(task: Task)

        /**
         * Deletes a task from the data layer by its ID.
         * @param taskId The ID of the task to be deleted.
         */
        fun deleteTaskById(taskId: Long)
    }
}
