package com.staygrateful.todolistapp.ui.edit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.domain.interactor.EditpageInteractor
import com.staygrateful.todolistapp.ui.edit.contract.EditpageContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val interactor: EditpageInteractor,
    application: Application
) : AndroidViewModel(application), EditpageContract.UserActionListener, DefaultLifecycleObserver {

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
}