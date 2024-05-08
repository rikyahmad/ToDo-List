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

@HiltViewModel
class EditViewModel @Inject constructor(
    private val interactor: EditpageInteractor,
    private val application: Application
) : AndroidViewModel(application), EditpageContract.UserActionListener, DefaultLifecycleObserver {

    var task: Task = Task.newInstance()

    override fun saveTask(task: Task, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            if(task.title.isEmpty()) {
                onResult.invoke(Exception(application.getString(R.string.error_form_empty_task)))
                return@launch
            }
            if(task.dueDate <= 0L) {
                onResult.invoke(Exception(application.getString(R.string.error_form_invalid_date)))
                return@launch
            }
            if(task.dueDate <= System.currentTimeMillis()) {
                onResult.invoke(Exception(application.getString(R.string.error_form_past_date)))
                return@launch
            }
            if (task.hasId) {
                interactor.updateTask(task)
            } else {
                interactor.insertTask(task)
            }
            onResult.invoke(null)
        }
    }
}