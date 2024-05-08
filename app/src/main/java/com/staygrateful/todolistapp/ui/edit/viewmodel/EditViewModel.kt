package com.staygrateful.todolistapp.ui.edit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import com.staygrateful.todolistapp.R
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.domain.interactor.EditpageInteractor
import com.staygrateful.todolistapp.ui.edit.contract.EditpageContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for managing data and business logic related to task editing.
 * Communicates with the interactor layer to perform data operations.
 * @param interactor The interactor responsible for interacting with the data layer.
 * @param application The application context.
 */
@HiltViewModel
class EditViewModel @Inject constructor(
    private val interactor: EditpageInteractor,
    private val application: Application
) : AndroidViewModel(application), EditpageContract.UserActionListener, DefaultLifecycleObserver {

    // Task being edited
    var task: Task = Task.newInstance()

    /**
     * Saves the task into the data layer.
     * Validates task details before saving.
     * @param task The task object to be saved.
     * @param onResult Callback function to handle the result of the save operation.
     */
    override fun saveTask(task: Task, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            // Perform validation checks before saving the task
            if(task.title.isEmpty()) {
                // Empty task title
                onResult.invoke(Exception(application.getString(R.string.error_form_empty_task)))
                return@launch
            }
            if(task.dueDate <= 0L) {
                // Invalid due date
                onResult.invoke(Exception(application.getString(R.string.error_form_invalid_date)))
                return@launch
            }
            if(task.dueDate <= System.currentTimeMillis()) {
                // Past due date
                onResult.invoke(Exception(application.getString(R.string.error_form_past_date)))
                return@launch
            }
            // Save or update the task based on whether it already has an ID
            if (task.hasId) {
                interactor.updateTask(task)
            } else {
                interactor.insertTask(task)
            }
            // Invoke the onResult callback with null to indicate successful operation
            onResult.invoke(null)
        }
    }
}
