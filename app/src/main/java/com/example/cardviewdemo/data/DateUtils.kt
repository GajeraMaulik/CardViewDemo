package com.example.cardviewdemo.data

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun fromMillisToTimeString(millis: Long?): String {
        val format = SimpleDateFormat("hh:mm ", Locale.getDefault())
        return format.format(millis)
    }
}