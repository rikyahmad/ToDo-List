package com.staygrateful.todolistapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.staygrateful.todolistapp.data.local.dao.TaskDao
import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskRepository(
    private val taskDao: TaskDao,
) {

    private val _allTasks = MutableLiveData<List<Task>>()

    val allTasks: LiveData<List<Task>> = _allTasks

    suspend fun getAllTasks(title: String) {
        taskDao.getAllTasks(title).collect { task ->
            _allTasks.value = task
        }
    }

    suspend fun getTaskById(taskId: Long): Task? {
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
