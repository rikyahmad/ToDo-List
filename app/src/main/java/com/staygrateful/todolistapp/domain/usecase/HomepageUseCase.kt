package com.staygrateful.todolistapp.domain.usecase

import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.flow.Flow

interface HomepageUseCase {

    val allTasks: Flow<List<Task>>
    fun getTaskById(taskId: Long): Task?
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteTaskById(taskId: Long)
}