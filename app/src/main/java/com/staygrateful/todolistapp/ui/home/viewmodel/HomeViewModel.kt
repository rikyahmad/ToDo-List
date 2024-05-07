package com.staygrateful.todolistapp.ui.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.domain.interactor.HomepageInteractor
import com.staygrateful.todolistapp.ui.home.contract.HomepageContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interactor: HomepageInteractor,
    application: Application
) : AndroidViewModel(application), HomepageContract.UserActionListener, DefaultLifecycleObserver {

    private val _allTasksLiveData: MutableLiveData<List<Task>> = MutableLiveData()

    val allTasksLiveData: LiveData<List<Task>> = interactor.allTasks

    fun getTaskList() {
        //_allTasksLiveData.value = interactor.allTasks
    }

    override fun getTaskById(taskId: Long): Task? {
        return interactor.getTaskById(taskId)
    }

    override fun insertTask(task: Task) {
        viewModelScope.launch {
            interactor.insertTask(task)
        }
    }

    override fun updateTask(task: Task) {
        viewModelScope.launch {
            interactor.updateTask(task)
        }
    }

    override fun deleteTask(task: Task) {
        viewModelScope.launch {
            interactor.deleteTask(task)
        }
    }

    override fun deleteTaskById(taskId: Long) {
        viewModelScope.launch {
            interactor.deleteTaskById(taskId)
        }
    }

}