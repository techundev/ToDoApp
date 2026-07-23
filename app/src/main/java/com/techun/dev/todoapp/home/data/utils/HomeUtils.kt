package com.techun.dev.todoapp.home.data.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateString(
    pattern: String = "dd/MM/yyyy HH:mm"
): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(this))
}