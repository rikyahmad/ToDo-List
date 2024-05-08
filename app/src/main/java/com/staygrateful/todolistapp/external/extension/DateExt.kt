package com.staygrateful.todolistapp.external.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Extension function to format a timestamp to a string with a specified pattern.
 *
 * This extension function takes a Long value representing a timestamp and converts it into a formatted string
 * according to the specified pattern. By default, the pattern is "dd/MM/yyyy HH:mm".
 *
 * @param pattern The pattern to use for formatting the timestamp. Default is "dd/MM/yyyy HH:mm".
 * @return A string representing the formatted timestamp.
 */
fun Long.getFormattedDate(pattern: String = "dd/MM/yyyy HH:mm"): String {
    // Create a SimpleDateFormat object with the specified pattern and default locale
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    // Format the timestamp using the SimpleDateFormat object and return the formatted string
    return dateFormat.format(Date(this))
}