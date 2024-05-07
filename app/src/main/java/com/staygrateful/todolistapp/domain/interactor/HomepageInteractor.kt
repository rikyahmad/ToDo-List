package com.staygrateful.todolistapp.domain.interactor

import androidx.lifecycle.LiveData
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.data.repository.TaskRepository
import com.staygrateful.todolistapp.domain.usecase.HomepageUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomepageInteractor @Inject constructor(
    private val taskRepository: TaskRepository
) : HomepageUseCase {

    override val allTasks: LiveData<List<Task>> = taskRepository.getAllTasks()

    override fun getTaskById(taskId: Long): Task? {
        return taskRepository.getTaskById(taskId)
    }

    override suspend fun insertTask(task: Task) {
        taskRepository.insertTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }

    override suspend fun deleteTaskById(taskId: Long) {
        taskRepository.deleteTaskById(taskId)
    }
}