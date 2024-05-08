package com.staygrateful.todolistapp.ui.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.domain.interactor.HomepageInteractor
import com.staygrateful.todolistapp.ui.home.contract.HomepageContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for managing data and business logic related to the home screen.
 * Communicates with the useCase layer to perform data operations.
 * @param useCase The useCase responsible for interacting with the data layer.
 * @param application The application context.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomepageInteractor,
    application: Application
) : AndroidViewModel(application), HomepageContract.UserActionListener, DefaultLifecycleObserver {

    // LiveData for observing the list of all tasks
    val allTasks: LiveData<List<Task>> = useCase.allTasks

    /**
     * Initializes the ViewModel by retrieving all tasks from the data layer.
     */
    init {
        getAllTasks()
    }

    /**
     * Retrieves all tasks from the data layer.
     * Optionally filters tasks by title.
     * @param title Optional parameter to filter tasks by title.
     */
    override fun getAllTasks(title: String) {
        viewModelScope.launch {
            useCase.getAllTasks(title)
        }
    }

    /**
     * Get upcoming tasks based on the due date.
     *
     * This method retrieves a list of tasks where the due date is greater than the current time,
     * ordered by due date in ascending order.
     *
     * @return A LiveData of List<Task> representing upcoming tasks.
     */
    override fun getUpcomingTasks(): Flow<List<Task>> {
        return useCase.getUpcomingTasks(System.currentTimeMillis())
    }

    /**
     * Inserts a new task into the data layer.
     * @param task The task object to be inserted.
     */
    override fun insertTask(task: Task) {
        viewModelScope.launch {
            useCase.insertTask(task)
        }
    }

    /**
     * Updates an existing task in the data layer.
     * @param task The task object to be updated.
     */
    override fun updateTask(task: Task) {
        viewModelScope.launch {
            useCase.updateTask(task)
        }
    }

    /**
     * Deletes a task from the data layer.
     * @param task The task object to be deleted.
     */
    override fun deleteTask(task: Task) {
        viewModelScope.launch {
            useCase.deleteTask(task)
        }
    }

    /**
     * Deletes a task from the data layer by its ID.
     * @param taskId The ID of the task to be deleted.
     */
    override fun deleteTaskById(taskId: Long) {
        viewModelScope.launch {
            useCase.deleteTaskById(taskId)
        }
    }
}
