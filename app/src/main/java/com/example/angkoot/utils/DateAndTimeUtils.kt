package com.example.angkoot.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateAndTimeUtils {
    private fun getCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        return calendar
    }

    fun getCurrentYear() = getCalendar().get(Calendar.YEAR)
    fun getCurrentMonth() = getCalendar().get(Calendar.MONTH) + 1
    fun getCurrentDay() = getCalendar().get(Calendar.DATE)
    fun getCurrentHour() = getCalendar().get(Calendar.HOUR_OF_DAY)

    fun isWeekday(): Boolean = getCalendar().get(Calendar.DAY_OF_WEEK) == 0

    fun isNight(): Boolean = getCurrentHour() > 18

    fun isLateNight(): Boolean {
        val hour = getCurrentHour()
        return hour in 0..5 || hour in 21..24
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime() =
        SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
            .format(Date())
}