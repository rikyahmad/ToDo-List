package com.staygrateful.todolistapp

import com.staygrateful.todolistapp.external.extension.getFormattedDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DateFormattingTest {

    @Test
    fun `test default pattern`() {
        val timestamp = 1620432000000 // 08 May 2021 00:00:00 GMT
        val expectedDate = "08/05/2021 07:00" // My Local Time GMT +7
        val formattedDate = timestamp.getFormattedDate()
        assertEquals(expectedDate, formattedDate)
    }

    @Test
    fun `test custom pattern`() {
        val timestamp = 1620432000000 // 08 May 2021 00:00:00 GMT
        val pattern = "yyyy-MM-dd"
        val expectedDate = "2021-05-08"
        val formattedDate = timestamp.getFormattedDate(pattern)
        assertEquals(expectedDate, formattedDate)
    }
}
