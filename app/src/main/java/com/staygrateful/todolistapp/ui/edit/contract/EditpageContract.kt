package com.staygrateful.todolistapp.ui.edit.contract

import androidx.lifecycle.LiveData
import com.staygrateful.todolistapp.data.model.Task

/**
 * Contract interface for defining interactions between the View and Presenter in the Editpage feature.
 */
interface EditpageContract {
    /**
     * View interface for defining methods to be implemented by the View layer.
     */
    interface View {
        // Add methods to be implemented by the View layer if needed
    }

    /**
     * UserActionListener interface for defining methods to be implemented by the Presenter layer.
     * This interface handles user actions and communicates with the data layer.
     */
    interface UserActionListener {
        /**
         * Saves the task into the data layer.
         * @param task The task object to be saved.
         * @param onResult Callback function to handle the result of the save operation.
         */
        fun saveTask(task: Task, onResult: (Throwable?) -> Unit)
    }
}
