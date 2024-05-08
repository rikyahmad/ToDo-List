package com.staygrateful.todolistapp.domain.interactor

import androidx.lifecycle.LiveData
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.data.repository.TaskRepository
import com.staygrateful.todolistapp.domain.usecase.EditpageUseCase
import com.staygrateful.todolistapp.domain.usecase.HomepageUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interactor class for handling edit page-related use cases.
 *
 * This class implements the EditpageUseCase interface and serves as an intermediary between the ViewModel
 * and the TaskRepository. It provides methods to insert and update tasks on the edit page.
 *
 * @param taskRepository The repository responsible for managing tasks data.
 */
class EditpageInteractor @Inject constructor(
    private val taskRepository: TaskRepository
) : EditpageUseCase {

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
}
