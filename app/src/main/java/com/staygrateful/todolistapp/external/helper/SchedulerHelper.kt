package com.staygrateful.todolistapp.external.helper

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import com.staygrateful.todolistapp.R
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.external.extension.showToast
import com.staygrateful.todolistapp.external.receiver.AlarmReceiver
import com.staygrateful.todolistapp.external.receiver.EarlyAlarmReceiver
import com.staygrateful.todolistapp.ui.home.view.HomeActivity

/**
 * Helper class for scheduling and canceling alarms.
 */
object SchedulerHelper {

    /**
     * Key for passing task in intent extras.
     */
    const val TASK = "TASK"

    /**
     * Key for passing task ID in intent extras.
     */
    const val TASK_ID = "TASK_ID"

    /**
     * Key for passing task updated in intent extras.
     */
    const val TASK_UPDATE = "TASK_UPDATE"

    private const val EARLY_TIME_MILLIS = 600_000L //10_000L

    /**
     * Schedules an alarm.
     * @param context The application context.
     * @param task The task associated with the alarm.
     * @param earlyTimeMillis The time before the dueDateTimeMillis to trigger an early alarm.
     */
    fun scheduleAlarm(
        context: Context,
        task: Task,
        earlyTimeMillis: Long = EARLY_TIME_MILLIS // 10 minutes
    ) {
        val applicationContext = context.applicationContext

        if (task.isCompleted) return
        if (task.dueDate <= System.currentTimeMillis()) return

        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(applicationContext, AlarmReceiver::class.java).apply {
            putExtra(TASK, task)
        }

        val earlyAlarmIntent = Intent(applicationContext, EarlyAlarmReceiver::class.java).apply {
            putExtra(TASK, task)
        }

        val openPendingIntent = PendingIntent.getActivity(
            applicationContext,
            task.id.toInt(),
            Intent(applicationContext, HomeActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            task.id.toInt(),
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val earlyPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            task.id.toInt(),
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
            val dismissalTriggerTime = task.dueDate - earlyTimeMillis

            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                0,
                dismissalTriggerTime,
                earlyPendingIntent
            )

            AlarmManagerCompat.setAlarmClock(
                alarmManager,
                task.dueDate,
                openPendingIntent,
                pendingIntent
            )
        } else {
            applicationContext.showToast(applicationContext.getString(R.string.error_to_schedule_alarm))
        }
    }

    /**
     * Cancels an alarm.
     * @param context The application context.
     * @param taskId The ID of the task associated with the alarm to be canceled.
     */
    fun cancelAlarm(context: Context, taskId: Long) {
        val applicationContext = context.applicationContext
        with(applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager) {
            NotificationHelper.cancelNotification(applicationContext, taskId)

            val alarmIntent = Intent(applicationContext, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                taskId.toInt(),
                alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val earlyAlarmIntent = Intent(applicationContext, EarlyAlarmReceiver::class.java)
            val earlyPendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                taskId.toInt(),
                earlyAlarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            cancel(pendingIntent)
            cancel(earlyPendingIntent)
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
