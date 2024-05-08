package com.staygrateful.todolistapp.external.helper

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import com.staygrateful.todolistapp.external.receiver.AlarmReceiver
import com.staygrateful.todolistapp.external.receiver.EarlyAlarmReceiver
import com.staygrateful.todolistapp.ui.home.view.HomeActivity

/**
 * Helper class for scheduling and canceling alarms.
 */
object AlarmSchedulerHelper {

    /**
     * Key for passing task ID in intent extras.
     */
    const val TASK_ID = "TASK_ID"

    /**
     * Schedules an alarm.
     * @param context The application context.
     * @param taskId The ID of the task associated with the alarm.
     * @param dueDateTimeMillis The time when the alarm should trigger.
     * @param earlyTimeMillis The time before the dueDateTimeMillis to trigger an early alarm.
     */
    fun scheduleAlarm(
        context: Context,
        taskId: Long,
        dueDateTimeMillis: Long,
        earlyTimeMillis: Long = 600_000L // 10 minutes
    ) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(TASK_ID, taskId)
        }

        val earlyAlarmIntent = Intent(context, EarlyAlarmReceiver::class.java).apply {
            putExtra(TASK_ID, taskId)
        }

        val openPendingIntent = PendingIntent.getActivity(
            context,
            taskId.toInt(),
            Intent(context, HomeActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val earlyPendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            earlyAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val canScheduleExactAlarms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

        if (canScheduleExactAlarms) {
            // Show a notification to allow dismissing the alarm 10 minutes before it actually triggers
            val dismissalTriggerTime = dueDateTimeMillis - earlyTimeMillis

            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                0,
                dismissalTriggerTime,
                earlyPendingIntent
            )

            AlarmManagerCompat.setAlarmClock(
                alarmManager,
                dueDateTimeMillis,
                openPendingIntent,
                pendingIntent
            )
        }
    }

    /**
     * Cancels an alarm.
     * @param context The application context.
     * @param taskId The ID of the task associated with the alarm to be canceled.
     */
    fun cancelAlarm(context: Context, taskId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, taskId.toInt(), alarmIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }

    /**
     * Checks if the app has the necessary permission for scheduling exact alarms.
     * @param context The application context.
     * @return true if the app has the necessary permission, false otherwise.
     */
    fun hasScheduleExactAlarmPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val packageManager = context.packageManager
            val permissionStatus = packageManager.checkPermission(
                Manifest.permission.SCHEDULE_EXACT_ALARM,
                context.packageName
            )
            return permissionStatus == PackageManager.PERMISSION_GRANTED
        }
        return true
    }
}
