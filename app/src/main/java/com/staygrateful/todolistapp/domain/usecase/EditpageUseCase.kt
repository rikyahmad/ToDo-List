package com.staygrateful.todolistapp.domain.usecase

import androidx.lifecycle.LiveData
import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Use case interface for edit page-related operations.
 *
 * This interface defines the contract for edit page-related operations,
 * such as inserting and updating tasks.
 */
interface EditpageUseCase {
    /**
     * Inserts a new task.
     *
     * This method is responsible for inserting a new task.
     *
     * @param task The task to insert.
     */
    suspend fun insertTask(task: Task)

    /**
     * Updates an existing task.
     *
     * This method is responsible for updating an existing task.
     *
     * @param task The task to update.
     */
    suspend fun updateTask(task: Task)
}
