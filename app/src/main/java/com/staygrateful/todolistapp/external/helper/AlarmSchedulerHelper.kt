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

object AlarmSchedulerHelper {

    const val TASK_ID = "TASK_ID"

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
            // show a notification to allow dismissing the alarm 10 minutes before it actually triggers
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

