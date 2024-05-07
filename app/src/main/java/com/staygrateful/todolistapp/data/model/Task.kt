package com.staygrateful.todolistapp.data.model

import android.icu.text.SimpleDateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.Locale

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String,
    val dueDate: Long,
    val isCompleted: Boolean = false
) {

    private fun getFormattedDueDate(dueDate: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(Date(dueDate))
    }

    var formattedDueDate: String? = null
        get() {
            if (field.isNullOrEmpty()) {
                field = getFormattedDueDate(dueDate)
            }
            return field
        }

}