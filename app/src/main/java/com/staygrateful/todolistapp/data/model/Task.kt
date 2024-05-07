package com.staygrateful.todolistapp.data.model

import android.icu.text.SimpleDateFormat
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.Locale

@Entity(tableName = "tasks")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var title: String,
    var description: String,
    var dueDate: Long,
    var isCompleted: Boolean = false
) : Parcelable {

    private fun getFormattedDueDate(dueDate: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(Date(dueDate))
    }

    val hasId: Boolean get() = id > 0

    @Ignore
    @IgnoredOnParcel
    var formattedDueDate: String? = null
        get() {
            if (field.isNullOrEmpty()) {
                field = getFormattedDueDate(dueDate)
            }
            return field
        }

    companion object {
        const val KEY = "key_task"

        fun newInstance(): Task = Task(0L, "", "", System.currentTimeMillis())
    }

}