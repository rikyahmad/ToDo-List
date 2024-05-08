package com.staygrateful.todolistapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationTask(
    val taskId: Long,
    val title: String,
    val message: String,
    val earlyNotification: Boolean
) : Parcelable
