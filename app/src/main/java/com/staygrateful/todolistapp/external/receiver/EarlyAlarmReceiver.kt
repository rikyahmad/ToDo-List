package com.staygrateful.todolistapp.external.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.staygrateful.todolistapp.external.helper.AlarmNotificationHelper
import com.staygrateful.todolistapp.external.helper.AlarmSchedulerHelper

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
        // Extract task ID from the intent
        val taskId = intent?.getLongExtra(AlarmSchedulerHelper.TASK_ID, -1)
        // Check if task ID is valid
        if (taskId != null && taskId != -1L) {
            // Display a toast message indicating the early due task
            Toast.makeText(context, "Early task with ID $taskId!", Toast.LENGTH_SHORT).show()
            // Show a notification for the early due task
            AlarmNotificationHelper.showAlarmNotification(
                context!!,
                taskId,
                "Early Alarm",
                "Alarm Akan Bunyi",
                true
            )
        }
    }
}
