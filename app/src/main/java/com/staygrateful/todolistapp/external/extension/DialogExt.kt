package com.staygrateful.todolistapp.external.extension

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.widget.TimePicker
import java.util.Locale

/**
 * Extension function to show a date-time picker dialog.
 *
 * This extension function displays a date-time picker dialog allowing the user to select both a date and a time.
 * It provides flexibility to specify the date-time format pattern and handles the selection with a callback.
 *
 * @param pattern The pattern to use for formatting the selected date-time. Default is "dd-MM-yyyy HH:mm".
 * @param onSelectedDate Callback function to handle the selected date-time. It provides the timestamp in milliseconds
 *                       and the formatted date-time string as parameters.
 */
fun Context.showDateTimePickerDialog(
    pattern: String = "dd-MM-yyyy HH:mm",
    onSelectedDate: (Long, String) -> Unit
) {
    // Initialize Calendar instances for selected and current dates
    val selectedDate = Calendar.getInstance()
    val currentDate = Calendar.getInstance()

    // Create a DatePickerDialog for selecting the date
    val datePicker = DatePickerDialog(
        this,
        { _, year, month, dayOfMonth ->
            // Set selected date values
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Create a TimePickerDialog for selecting the time
            val timeListener =
                TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
                    // Set selected time values
                    selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedDate.set(Calendar.MINUTE, minute)

                    // Format the selected date-time using the specified pattern
                    val dateTimeFormat = SimpleDateFormat(pattern, Locale.getDefault())
                    val dateTime = dateTimeFormat.format(selectedDate.time)

                    // Invoke the callback function with the selected timestamp and formatted date-time
                    onSelectedDate.invoke(selectedDate.timeInMillis, dateTime)
                }

            // Get current hour and minute values
            val hour = currentDate.get(Calendar.HOUR_OF_DAY)
            val minute = currentDate.get(Calendar.MINUTE)

            // Show the TimePickerDialog
            TimePickerDialog(this, timeListener, hour, minute, true).show()
        },
        // Set DatePickerDialog initial date to current date
        currentDate.get(Calendar.YEAR),
        currentDate.get(Calendar.MONTH),
        currentDate.get(Calendar.DAY_OF_MONTH)
    )

    // Set minimum date to today
    datePicker.datePicker.minDate = System.currentTimeMillis() - 1000

    // Show the DatePickerDialog
    datePicker.show()
}
