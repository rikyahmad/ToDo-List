package com.staygrateful.todolistapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.staygrateful.todolistapp.data.local.dao.TaskDao
import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskRepository(
    private val taskDao: TaskDao,
) {

    private val _allTasks = MutableLiveData<List<Task>>()

    val allTasks: LiveData<List<Task>> = _allTasks

    private var searchFilter: String = ""

    suspend fun getAllTasks(title: String) {
        _allTasks.value = taskDao.getAllTasks(title)
        searchFilter = title
    }

    suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
        getAllTasks(searchFilter)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
        getAllTasks(searchFilter)
    }

    suspend fun deleteTask(task: Task) {
        // use query to get result row count
        deleteTaskById(task.id)
    }

    suspend fun deleteTaskById(taskId: Long) {
        taskDao.deleteTaskById(taskId).also { result ->
            println("Delete Result : $result")
            if(result > 0) {
                getAllTasks(searchFilter)
            }
        }
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
        getAllTasks(searchFilter)
    }
}
