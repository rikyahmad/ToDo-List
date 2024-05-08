package com.staygrateful.todolistapp.external.extension

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.widget.TimePicker
import java.util.Locale

fun Context.showDateTimePickerDialog(
    pattern: String = "dd-MM-yyyy HH:mm",
    onSelectedDate: (Long, String) -> Unit
) {
    val selectedDate = Calendar.getInstance()
    val currentDate = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        this,
        { _, year, month, dayOfMonth ->
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val timeListener =
                TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
                    selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedDate.set(Calendar.MINUTE, minute)

                    val dateTimeFormat = SimpleDateFormat(pattern, Locale.getDefault())
                    val dateTime = dateTimeFormat.format(selectedDate)

                    onSelectedDate.invoke(selectedDate.timeInMillis, dateTime)
                }

            val hour = currentDate.get(Calendar.HOUR_OF_DAY)
            val minute = currentDate.get(Calendar.MINUTE)

            TimePickerDialog(this, timeListener, hour, minute, true).show()
        },
        currentDate.get(Calendar.YEAR),
        currentDate.get(Calendar.MONTH),
        currentDate.get(Calendar.DAY_OF_MONTH)
    )

    datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
    datePicker.show()
}