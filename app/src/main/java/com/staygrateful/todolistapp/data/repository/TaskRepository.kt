package com.staygrateful.todolistapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.staygrateful.todolistapp.data.local.dao.TaskDao
import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao,
) {

    fun getAllTasks(): LiveData<List<Task>> {
        return taskDao.getAllTasks()
    }

    fun getAllCompletedTasks(): LiveData<List<Task>> {
        return taskDao.getAllCompletedTasks()
    }

    fun getAllPendingTasks(): LiveData<List<Task>> {
        return taskDao.getAllPendingTasks()
    }

    fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteTaskById(taskId: Long) {
        taskDao.deleteTaskById(taskId)
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}
