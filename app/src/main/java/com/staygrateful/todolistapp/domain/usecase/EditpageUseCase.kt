package com.staygrateful.todolistapp.domain.usecase

import androidx.lifecycle.LiveData
import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.flow.Flow

interface EditpageUseCase {
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
}