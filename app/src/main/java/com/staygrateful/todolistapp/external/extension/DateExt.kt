package com.staygrateful.todolistapp.external.extension

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.getFormattedDate(pattern: String = "dd/MM/yyyy HH:mm"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(Date(this))
}