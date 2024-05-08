package com.staygrateful.todolistapp.external.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.IntentCompat
import com.staygrateful.todolistapp.R
import com.staygrateful.todolistapp.data.model.NotificationTask
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.external.helper.SchedulerHelper
import com.staygrateful.todolistapp.external.services.TaskService

/**
 * BroadcastReceiver for handling early alarm events.
 *
 * This class is responsible for receiving early alarm broadcasts and performing actions accordingly.
 * When an early alarm is triggered, it displays a toast message and shows a notification for the early due task.
 */
class EarlyAlarmReceiver : BroadcastReceiver() {

    /**
     * Called when a broadcast is received.
     *
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     * It handles early alarm events by displaying a toast message and showing a notification for the early due task.
     *
     * @param context The context in which the receiver is running.
     * @param intent The Intent being received.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent == null) return
        // Extract task from the intent
        val task =
            IntentCompat.getParcelableExtra(intent, SchedulerHelper.TASK, Task::class.java)
        // Check if task ID is valid
        if (task != null) {
            // Show a notification for the due task
            TaskService.postNotification(
                context,
                NotificationTask(
                    task.id,
                    context.getString(R.string.notification_upcoming_task, task.title),
                    context.getString(R.string.notification_upcoming_message),
                    true
                )
            )
        }
    }
}
