package com.staygrateful.todolistapp.ui.edit.contract

import androidx.lifecycle.LiveData
import com.staygrateful.todolistapp.data.model.Task

class EditpageContract {
    interface View {
        //fun onPermissionResult()
    }

    interface UserActionListener {
        fun saveTask(task: Task, onResult: (Throwable?) -> Unit)
    }
}