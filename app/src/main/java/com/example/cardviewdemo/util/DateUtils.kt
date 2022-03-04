package com.example.cardviewdemo.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun formatTime(millis: Long?): String {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(millis)
    }

    fun formatDate(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
        return dateFormat.format(timeInMillis)
    }
}