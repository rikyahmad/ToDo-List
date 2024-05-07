package com.staygrateful.todolistapp.external.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.staygrateful.todolistapp.external.helper.AlarmNotificationHelper
import com.staygrateful.todolistapp.external.helper.AlarmSchedulerHelper

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val taskId = intent?.getLongExtra(AlarmSchedulerHelper.TASK_ID, -1)
        if (taskId != null && taskId != -1L) {
            Toast.makeText(context, "Task with ID $taskId is due!", Toast.LENGTH_SHORT).show()
            AlarmNotificationHelper.showAlarmNotification(context!!, taskId, "Alarm Running", "Alarm Bunyi", true)
        }
    }
}
