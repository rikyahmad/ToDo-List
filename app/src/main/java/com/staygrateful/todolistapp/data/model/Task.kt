package com.staygrateful.todolistapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.staygrateful.todolistapp.external.extension.getFormattedDate
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

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

    val hasId: Boolean get() = id > 0

    @Ignore
    @IgnoredOnParcel
    var formattedDueDate: String? = null
        get() {
            if (field.isNullOrEmpty()) {
                field = dueDate.getFormattedDate()
            }
            return field
        }

    companion object {
        const val KEY = "key_task"

        fun newInstance(): Task = Task(0L, "", "", System.currentTimeMillis())
    }
}