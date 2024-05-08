package com.staygrateful.todolistapp.external.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.staygrateful.todolistapp.external.helper.NotificationHelper
import com.staygrateful.todolistapp.external.helper.SchedulerHelper
import com.staygrateful.todolistapp.external.services.TaskService

/**
 * BroadcastReceiver for handling task completion events.
 *
 * This class is responsible for receiving task completion broadcasts and performing actions accordingly.
 * When a task is completed, it displays a toast message and cancels any existing notifications for the task.
 */
class TaskCompletedReceiver : BroadcastReceiver() {

    /**
     * Called when a broadcast is received.
     *
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     * It handles task completion events by displaying a toast message and canceling any existing notifications for the task.
     *
     * @param context The context in which the receiver is running.
     * @param intent The Intent being received.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent == null) return
        // Extract task ID from the intent
        val taskId = intent.getLongExtra(SchedulerHelper.TASK_ID, -1)
        // Check if task ID is valid
        if (taskId != -1L) {
            // Cancel any existing notifications for the completed task
            NotificationHelper.cancelNotification(context, taskId)
            TaskService.completionUpdate(context, taskId)
        }
    }
}
