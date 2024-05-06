package com.staygrateful.todolistapp.ui.home.contract

import com.staygrateful.todolistapp.data.model.Task

class HomepageContract {
    interface View {
        //fun onPermissionResult()
    }

    interface UserActionListener {

        //fun setupListener(activity: ComponentActivity)
        fun getTaskById(taskId: Long): Task?
        fun insertTask(task: Task)
        fun updateTask(task: Task)
        fun deleteTask(task: Task)
        fun deleteTaskById(taskId: Long)
    }
}