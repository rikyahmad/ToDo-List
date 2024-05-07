package com.staygrateful.todolistapp.domain.interactor

import androidx.lifecycle.LiveData
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.data.repository.TaskRepository
import com.staygrateful.todolistapp.domain.usecase.EditpageUseCase
import com.staygrateful.todolistapp.domain.usecase.HomepageUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditpageInteractor @Inject constructor(
    private val taskRepository: TaskRepository
) : EditpageUseCase {

    override suspend fun insertTask(task: Task) {
        taskRepository.insertTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }
}