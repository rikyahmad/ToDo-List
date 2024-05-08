package com.staygrateful.todolistapp.external.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.staygrateful.todolistapp.R
import com.staygrateful.todolistapp.external.receiver.TaskCompletedReceiver
import com.staygrateful.todolistapp.ui.home.view.HomeActivity

/**
 * Helper class for managing alarm notifications.
 */
object AlarmNotificationHelper {

    private const val CHANNEL_ID = "alarm_channel"
    private const val EARLY_CHANNEL_ID = "early_alarm_channel"
    private const val CHANNEL_NAME = "Alarm Channel"
    private const val NOTIFICATION_ID = 123

    /**
     * Shows an alarm notification.
     * @param context The application context.
     * @param taskId The ID of the task associated with the alarm.
     * @param title The title of the notification.
     * @param message The message content of the notification.
     * @param earlyNotification Indicates if it's an early notification.
     */
    fun showAlarmNotification(
        context: Context,
        taskId: Long,
        title: String,
        message: String,
        earlyNotification: Boolean = false
    ) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val nb = createNotificationBuilder(
            context,
            taskId,
            title,
            message,
            if (earlyNotification) EARLY_CHANNEL_ID else CHANNEL_ID,
            CHANNEL_NAME,
            context.getString(R.string.notification_channel_alarm),
            earlyNotification = earlyNotification
        )

        nm.notify(NOTIFICATION_ID, nb.build())
    }

    /**
     * Cancels the alarm notification.
     * @param context The application context.
     */
    fun cancelNotification(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIFICATION_ID)
    }

    /**
     * Creates a notification builder.
     * @param context The application context.
     * @param taskId The ID of the task associated with the notification.
     * @param title The title of the notification.
     * @param text The text content of the notification.
     * @param channelID The ID of the notification channel.
     * @param channelName The name of the notification channel.
     * @param channelDesc The description of the notification channel.
     * @param earlyNotification Indicates if it's an early notification.
     * @param priority The priority of the notification.
     * @return A NotificationCompat.Builder object.
     */
    fun createNotificationBuilder(
        context: Context,
        taskId: Long,
        title: String,
        text: String,
        channelID: String,
        channelName: String,
        channelDesc: String,
        earlyNotification: Boolean = false,
        priority: Int = NotificationCompat.PRIORITY_MAX
    ): NotificationCompat.Builder {

        // Ensure the notification channel is created
        createNotificationChannelIfNecessary(
            context,
            channelID,
            channelName,
            channelDesc,
            earlyNotification
        )

        val opennedPI = PendingIntent.getActivity(
            context,
            taskId.toInt(),
            Intent(context, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(AlarmSchedulerHelper.TASK_ID, taskId)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val completedPI = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            Intent(context, TaskCompletedReceiver::class.java).apply {
                putExtra(AlarmSchedulerHelper.TASK_ID, taskId)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context.applicationContext, channelID)
            .setOngoing(false)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_delete) // Required.
            .setPriority(priority)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            .setCategory(Notification.CATEGORY_REMINDER)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(opennedPI)
            .addAction(
                R.drawable.ic_delete,
                context.getString(R.string.notification_set_completed), completedPI
            )
            .setContentTitle(title) // Required.
            .setContentText(text) // Required.
    }

    /**
     * Creates a notification channel if necessary.
     * @param context The application context.
     * @param channelID The ID of the notification channel.
     * @param channelName The name of the notification channel.
     * @param channelDesc The description of the notification channel.
     * @param earlyNotification Indicates if it's an early notification.
     */
    fun createNotificationChannelIfNecessary(
        context: Context,
        channelID: String,
        channelName: String,
        channelDesc: String,
        earlyNotification: Boolean = false
    ) {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val manager = context.getSystemService(Service.NOTIFICATION_SERVICE)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setLegacyStreamType(AudioManager.STREAM_ALARM)
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .build()

        if (manager is NotificationManager) {
            if (manager.getNotificationChannel(channelID) == null) {
                // Clean up previous notification channel with sound properties
                oldNotificationChannelCleanup(manager, channelID)
                NotificationChannel(
                    channelID,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = channelDesc
                    lightColor = Color.BLUE
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    if (earlyNotification) {
                        // Delay, run, delay, run...
                        vibrationPattern = longArrayOf(100L, 1000L, 500L, 1000L)
                    } else {
                        // Delay, run, delay, run...
                        vibrationPattern = longArrayOf(100L, 2000L, 1000L, 2000L, 1000L, 2000L)
                        setSound(soundUri, audioAttributes)
                    }
                    setBypassDnd(true)
                    enableLights(true)
                    enableVibration(true)
                    manager.createNotificationChannel(this)
                }
            }
        }
    }

    /**
     * Cleans up the old notification channel.
     * @param notificationManager The NotificationManager object.
     * @param channelId The ID of the notification channel to be cleaned up.
     */
    fun oldNotificationChannelCleanup(notificationManager: NotificationManager, channelId: String) {
        try {
            notificationManager.deleteNotificationChannel(channelId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
