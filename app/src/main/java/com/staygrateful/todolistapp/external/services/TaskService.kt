package com.staygrateful.todolistapp.external.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.IntentCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.staygrateful.todolistapp.data.local.database.TaskDatabase
import com.staygrateful.todolistapp.data.model.NotificationTask
import com.staygrateful.todolistapp.external.helper.NotificationHelper
import com.staygrateful.todolistapp.external.helper.SchedulerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null) {
            val taskType = intent.getIntExtra(TASK_TYPE, -1)
            if (taskType == TYPE_UPDATE_COMPLETION) {
                val taskId = intent.getLongExtra(TASK_DATA, -1)

                if (taskId != -1L) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val taskDao = TaskDatabase.getDatabase(applicationContext).taskDao()
                        taskDao.updateTaskCompletion(taskId, true)

                        LocalBroadcastManager.getInstance(applicationContext)
                            .sendBroadcast(Intent(SchedulerHelper.TASK_UPDATE).apply {
                                putExtra(SchedulerHelper.TASK_ID, taskId)
                            })
                    }
                }
            } else if (taskType == TYPE_POST_NOTIFICATION) {
                val notificationTask = IntentCompat.getParcelableExtra(intent, TASK_DATA, NotificationTask::class.java)
                if(notificationTask != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val taskDao = TaskDatabase.getDatabase(applicationContext).taskDao()
                        val task = taskDao.getTaskById(notificationTask.taskId)
                        if(task != null && !task.isCompleted) {
                            NotificationHelper.showAlarmNotification(
                                applicationContext,
                                notificationTask.taskId,
                                notificationTask.title,
                                notificationTask.message,
                                notificationTask.earlyNotification
                            )
                        }
                    }
                }
            }
        }

        stopSelf() // Stop the service after completing the task
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {

        private const val TASK_TYPE = "TASK_TYPE"
        private const val TASK_DATA = "TASK_DATA"
        private const val TYPE_UPDATE_COMPLETION = 1
        private const val TYPE_POST_NOTIFICATION = 2

        fun completionUpdate(context: Context, taskId: Long) {
            val serviceIntent = Intent(context, TaskService::class.java).apply {
                putExtra(TASK_TYPE, TYPE_UPDATE_COMPLETION)
                putExtra(TASK_DATA, taskId)
            }
            context.applicationContext.startService(serviceIntent)
        }

        fun postNotification(context: Context, notificationTask: NotificationTask) {
            val serviceIntent = Intent(context, TaskService::class.java).apply {
                putExtra(TASK_TYPE, TYPE_POST_NOTIFICATION)
                putExtra(TASK_DATA, notificationTask)
            }
            context.applicationContext.startService(serviceIntent)
        }
    }
}
